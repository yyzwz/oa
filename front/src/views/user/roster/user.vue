<template>
  <div>
    <span style="color:#ff0000">当前选择人员：{{form.userName}}</span>
    <!-- {{departmentNameData}} -->
    <card>
      <div>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32">
            <Divider>
              <Button v-if="!isEdit" type="error" :loading="submitLoading" @click="submit">保存信息</Button>
              <Button v-if="isEdit" type="error" :loading="submitLoading" @click="beginEdit">开始编辑</Button>
            </Divider>
            <Col span="3">
              <FormItem label="工号">
                <Input v-model="form.jobNumber" :readonly="isEdit"/>
              </FormItem>
            </Col>
            <Col span="3">
              <FormItem label="姓名">
                <Input v-model="form.userName" :readonly="isEdit"/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="手机号">
                <Input v-model="form.mobile" :readonly="isEdit" maxlength="11" show-word-limit />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="身份证号码">
                <Input v-model="form.idCard" :readonly="isEdit" @on-change="changeIdcard" maxlength="18" show-word-limit />
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">
            <Col span="6">
              <FormItem label="Email">
                <Input v-model="form.email" :readonly="isEdit"/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="民族">
                <Select v-model="form.nation" :disabled="isEdit">
                  <Option
                    v-for="(item, i) in this.$store.state.dict.nation"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="婚姻状态">
                <Select v-model="form.love" :disabled="isEdit">
                  <Option
                    v-for="(item, i) in this.$store.state.dict.marriage"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="政治面貌">
                <Select v-model="form.face" :disabled="isEdit">
                  <Option
                    v-for="(item, i) in this.$store.state.dict.face"
                    :key="i"
                    :value="item.value"
                  >{{item.title}}</Option>
                </Select>
              </FormItem>
            </Col>
          </Row>
           
          <Divider><span style="color:#ff0000">自动填写</span></Divider>
          <Row :gutter="32">  
            <Col span="8">
              <FormItem label="性别">
                <Input v-model="form.sex" readonly/>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="年龄">
                <Input v-model="form.age" readonly/>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="生日">
                <Input v-model="form.birthday" readonly/>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32">  
            <Col span="8">
              <FormItem label="籍贯">
                <Input v-model="form.nativePlace" readonly/>
              </FormItem>
            </Col>
            
            <Col span="8">
              <FormItem label="人员ID">
                <Input v-model="form.id" readonly/>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="钉钉号">
                <Input v-model="form.dingCode" readonly/>
              </FormItem>
            </Col>
          </Row>
          <Divider><span style="color:#ff0000">地址</span></Divider>
          <Row :gutter="32">  
            <Col span="24">
              <FormItem label="家庭地址">
                <Input v-model="form.address" :readonly="isEdit" maxlength="50" show-word-limit/>
              </FormItem>
            </Col>
          </Row>
          <Divider><span style="color:#ff0000">教育背景</span></Divider>
          <Row :gutter="32">
            <Col span="12">
              <FormItem label="毕业学校">
                <Input v-model="form.colleges" :readonly="isEdit" maxlength="20" show-word-limit/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="专业">
                <Input v-model="form.major" :readonly="isEdit" maxlength="20" show-word-limit/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="学历">
                <Input v-model="form.education" :readonly="isEdit" maxlength="20" show-word-limit/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="毕业时间">
                <DatePicker :readonly="isEdit" type="date" :placeholder="firststyledateTemp" v-model="form.graduationTime"  @on-change="changeFirststyledate"></DatePicker>
              </FormItem>
            </Col>
          </Row>
        </Form>
      </div>
    </card>
  </div>
</template>

<script>
import {getDepartmentList,getUserByDepId,deleteDingUser,updateDingUser,getOneHua,addHua,editHuaOnDing,getCityByIdcard} from "./index";
import addEditUser from './addEditUser';
// 编辑保安管理范围列表组件
export default {
  name: "anbao-fanwei",
  components: {
    addEditUser
  },
  data() {
    return {
      isChangeHighstyledate:false,
      isChangeFirststyledate:false,
      firststyledateTemp:'',
      isEdit:true,
      changeBirthday:false,
      birthdayTemp: '请选择生日',
      departmentNameData:'未选择',
      form:{
        id:'未选择',
      }
    }
  },
  methods: {
    init() {
      // this.getUserList();
    },
    beginEdit() {
      this.isEdit = false;
    },
    changeIdcard(){
      var idcard = this.form.idCard;
      if(idcard.length == 18){
        // 性别
        if(idcard[16] == '1'){
          this.form.sex = "男";
        }
        else{
          this.form.sex = "女";
        }

        // 出生地
        var cityId = idcard.substring(0,6);
        getCityByIdcard({cityIds: cityId}).then(res =>{
          this.form.nativePlace = res;
        })
        var birthYear = idcard.substring(6,10);
        this.form.age = new Date().getFullYear() - birthYear + "";

        // 生日
        var birthday = idcard.substring(6,10) + "-" 
                    + idcard.substring(10,12) + "-"
                    + idcard.substring(12,14);
        this.form.birthday = birthday;
      }
    },
    getUserList(){
      getOneHua({id:this.departmentNameData}).then(res =>{
        this.form = res.result;
        if(this.form.year != null){
          this.form.birthday = this.form.year + "-" + this.form.mouth + "-" + this.form.day;
          // this.birthdayTemp = this.form.year + '-' +  this.form.mouth + '-' + this.form.day;
        }else{
          this.birthdayTemp = '未填写';
        }
        this.firststyledateTemp = this.form.firststyledate;
        this.highstyledateTemp = this.form.highstyledate;
      })
    },
    changeFirststyledate(){
      this.isChangeFirststyledate = true;
    },
    changeHighstyledate(){
      this.isChangeHighstyledate = true;
    },
    submit() {
      var fd = new Date(this.form.graduationTime);
      this.form.graduationTime = fd.getFullYear() + '-' + (fd.getMonth() + 1) + '-' + fd.getDate();
      if(this.form.jobNumber == null) delete this.form.jobNumber;
      if(this.form.userName == null) delete this.form.userName;
      if(this.form.idCard == null) delete this.form.idCard;
      if(this.form.email == null) delete this.form.email;
      if(this.form.sex == null) delete this.form.sex;
      if(this.form.age == null) delete this.form.age;
      if(this.form.birthday == null) delete this.form.birthday;
      if(this.form.nativePlace == null) delete this.form.nativePlace;
      if(this.form.dingCode == null) delete this.form.dingCode;
      if(this.form.address == null) delete this.form.address;
      if(this.form.colleges == null) delete this.form.colleges;
      if(this.form.graduationTime == null) delete this.form.graduationTime;
      if(this.form.major == null) delete this.form.major;
      if(this.form.education == null) delete this.form.education;
      if(this.form.nation == null) delete this.form.nation;
      if(this.form.love == null) delete this.form.love;
      if(this.form.face == null) delete this.form.face;
      if(this.form.introduce == null) delete this.form.introduce;
      this.form.status = 0;
      this.form.updateTime = this.form.createTime;
      editHuaOnDing(this.form).then(res =>{
        if(res.success == true){
          this.isEdit = true;
          this.$emit("on-submit", true);
        }
      }) 
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
