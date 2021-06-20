<template>
  <div>
    <span style="color:#ff0000">当前选择人员：{{form.rosterName}}</span>
    <!-- {{departmentNameData}} -->
    <card>
      <div>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32">
            <Divider>
              <Button v-if="!isEdit" type="error" :loading="submitLoading" @click="submit">保存信息</Button>
              <Button v-if="isEdit" type="error" :loading="submitLoading" @click="beginEdit">开始编辑</Button>
            </Divider>
            <Col span="6">
              <FormItem label="人员ID">
                <Input v-model="form.rosterId" readonly/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="姓名">
                <Input v-model="form.rosterName" readonly/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="岗位岗级">
                <Input v-model="form.postLevelName" readonly/>
                <postLevelChoose @on-change="changePostLevelChoose" ref="postLevelChoose" :disabled="isEdit"></postLevelChoose>
              </FormItem>
            </Col>
          </Row>
          <Divider><span style="color:#ff0000">合同</span></Divider>
          <Row :gutter="32">
            <Col span="8">
              <FormItem label="合同日期">
                <DatePicker type="date" :placeholder="contractDateTemp" v-model="form.contractDateTemp" @on-change="changecontractDate" :readonly="isEdit"></DatePicker>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="合同期限(年)">
                <InputNumber :max="12" :min="1" v-model="form.contractPeriod" :readonly="isEdit" @on-change="changecontractPeriod"></InputNumber>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="合同到期">
                <Input v-model="form.contractExpire" readonly/>
              </FormItem>
            </Col>
          </Row>
          <Divider><span style="color:#ff0000">转正</span></Divider>
          <Row :gutter="32">
            <Col span="8">
              <FormItem label="入职时间">
                <DatePicker type="date" :placeholder="entryTimeTemp" v-model="form.entryTimeTemp" @on-change="changeentryTime" :readonly="isEdit"></DatePicker>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="试用期（月）">
                <InputNumber :max="12" :min="1" v-model="form.probationPeriod" :readonly="isEdit" @on-change="changeShiYong">月</InputNumber>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="转正日期">
                <Input v-model="form.becomeFormal" readonly/>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="社保">
                <Select v-model="form.socialInsurance" clearable :disabled="isEdit">
                  <Option
                        v-for="(item, i) in this.sociologyList"
                        :key="i"
                        :value="item.id"
                      >{{item.insuranceName}}
                  </Option>
                </Select>
              </FormItem>
            </Col>

            <Col span="8">
              <FormItem label="商保">
                <Select v-model="form.commercialInsurance" clearable :disabled="isEdit">
                  <Option
                        v-for="(item, i) in this.businessList"
                        :key="i"
                        :value="item.id"
                      >{{item.insuranceName}}
                  </Option>
                </Select>
              </FormItem>
            </Col>
            
          </Row>
        </Form>
      </div>
    </card>
  </div>
</template>

<script>
import {
  getSociology,
  getBusiness,
  getBaoXianById,
  getAllSheBao,
  getAllShangBao,
  getAllBaoXian,
  getNong,
  getAllGuiShu,
  getDepartmentList,
  getUserByDepId,
  deleteDingUser,
  updateDingUser,
  getOneHua,
  getOnePost,
  addHua,
  setHuaGang,
  getAllGangJi,
  getAllGangWei,
  getAllZhiWu
  } from "./index";
import addEditUser from './addEditUser';
import postLevelChoose from "./post-level-choose.vue";
// 编辑保安管理范围列表组件
export default {
  name: "anbao-fanwei",
  components: {
    addEditUser,
    postLevelChoose
  },
  data() {
    return {
      isEdit:true,
      changeBirthday:false,
      birthdayTemp: '请选择生日',
      departmentNameData:'未选择',
      form:{
        id:'未选择',
      },
      gangJiList:[],
      gangWeiList:[],

      entryTimeTemp:'请选择入职时间',
      ischangeentryTime:false,

      contractDateTemp:'请选择合同日期',
      ischangecontractDateDate:false,

      contractExpireTemp:'合同到期时间',
      ischangecontractExpireDate:false,

      zhuanZhengTemp:'转正时间',
      ischangeZhuanZhengDate:false,

      businessList:[],
      sociologyList:[],
    }
  },
  methods: {
    init() {
      // this.getUserList();
      // this.getAllGangJi();
      // this.getAllGangWei();
      // this.getAllZhiWu();
      // this.getAllGuiShu();
      // this.getAllBaoXian();
      this.getBusiness();
      this.getSociology();
    },
    changePostLevelChoose(e){
      console.log(e);
      if(e.postLevelId != undefined){
        this.form.postLevelId = e.postLevelId;
        this.form.postLevelName = e.postLevelName;
      }else{
        this.form.postLevelId = '1294462401827180544';
        this.form.postLevelName = "未设置岗级";
      }
    },
    //获取所有商保
    getBusiness(){
      getBusiness().then(res => {
        this.businessList = res.result;
      })
    },
    //获取所有社保
    getSociology(){
      getSociology().then(res => {
        this.sociologyList = res.result;
      })
    },
    changecontractPeriod(){
      var qi = parseInt(this.form.contractPeriod);
      var a = new Date(this.form.contractDateTemp);
      var year = a.getFullYear() + qi;
      var mouth = a.getMonth() + 1;
      var day = a.getDate();
      this.form.contractExpire = year + '-' + mouth + '-' + day;
    },
    changeShiYong(){
      var qi = this.form.probationPeriod;
      var a = new Date(this.form.entryTimeTemp);
      var year = a.getFullYear();
      var mouth = a.getMonth() + 1 + qi;
      var day = a.getDate();
      if(mouth > 12) {
        mouth -= 12;
        year += 1;
      }
      this.form.becomeFormal = year + '-' + mouth + '-' + day;
    },
    // 入职时间
    changeentryTime(){
      this.ischangeentryTime = true;
      var a = new Date(this.form.entryTimeTemp);
      this.form.entryTime = a.getFullYear() + '-' + (a.getMonth() + 1) + '-' + a.getDate();
    },
    // 合同日期
    changecontractDate(){
      this.ischangecontractDateDate = true;
      var b = new Date(this.form.contractDateTemp);
      this.form.contractDate = b.getFullYear() + '-' + (b.getMonth() + 1) + '-' + b.getDate();
    },
    // 合同到期
    changecontractExpire(){
      this.ischangecontractExpireDate = true;
      var c = new Date(this.form.contractExpireTemp);
      this.form.contractExpire = c.getFullYear() + '-' + (c.getMonth() + 1) + '-' + c.getDate();
    },
    // 转正日期
    changeZhuanZheng(){
      this.ischangeZhuanZhengDate = true;
      var d = new Date(this.form.zhuanZhengTemp);
      this.form.zhuanZheng = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
    },
    shebaoFouChangeSwitch(status){
      this.form.shebaoFou = "" + status;
    },
    shangBaoChangeSwitch(status){
      this.form.shangBao = "" + status;
    },
    getAllBaoXian(){
      getAllBaoXian().then(res =>{
        this.baoXianList = res.result;
      })
    },
    getAllGangJi(){
      getAllGangJi().then(res =>{
        this.gangJiList = res.result;
      })
    },
    getAllGangWei(){
      getAllGangWei().then(res =>{
        this.gangWeiList = res.result;
      })
    },
    getAllZhiWu(){
      getAllZhiWu().then(res =>{
        this.ZhiWuList = res.result;
      })
    },
    getDateData(){
      this.entryTimeTemp = this.form.entryTime;
      this.contractDateTemp = this.form.contractDate;
      this.contractExpireTemp = this.form.contractExpire;
      this.zhuanZhengTemp = this.form.zhuanZheng;
    },
    beginEdit() {
      this.isEdit = false;
    },
    getUserList(){
      getOnePost({id:this.departmentNameData}).then(res =>{
        console.log(res);
        this.form = res.result;
        // this.$refs.postLevelChoose.setData(this.form.post, this.form.postName);
        this.getDateData();
      })
      
    },
    submit() {
      this.form.updateTime = this.form.createTime;
      if(this.form.post == null) delete this.form.post;
      if(this.form.postLevel == null) delete this.form.postLevel;
      if(this.form.contractDateTemp == null) delete this.form.contractDateTemp;
      if(this.form.contractDate == null) delete this.form.contractDate;
      if(this.form.contractPeriod == null) delete this.form.contractPeriod;
      if(this.form.contractExpire == null) delete this.form.contractExpire;
      if(this.form.entryTimeTemp == null) delete this.form.entryTimeTemp;
      if(this.form.entryTime == null) delete this.form.entryTime;
      if(this.form.probationPeriod == null) delete this.form.probationPeriod;
      if(this.form.becomeFormal == null) delete this.form.becomeFormal;
      
      setHuaGang(this.form).then(res =>{
      })
      this.isEdit = true;
      this.$emit("on-submit", true);
    },
    calAge() {
      this.changeBirthday = true;
      this.birthdayTemp = this.form.birthday;
      var a = new Date(this.form.birthday);
      this.form.age = new Date().getFullYear() - a.getFullYear() + 1;
    }
  },
  mounted() {
    this.init();
  },
  props: ['departmentName'],
  watch:{
      departmentName:function(newVal,oldVal){
        this.departmentNameData = newVal;
        this.getUserList();
      }
  },
   
}
</script>

<style>

</style>
