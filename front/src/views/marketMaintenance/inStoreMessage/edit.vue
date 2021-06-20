<template>
  <div>
    <Card>
      <div slot="title">
        <div class="edit-head">
          <a @click="close" class="back-title">
            <Icon type="ios-arrow-back" />返回
          </a>
          <div class="head-name">编辑</div>
          <span></span>
          <a @click="close" class="window-close">
            <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
          </a>
        </div>
      </div>
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >        
        <FormItem label="负责人" prop="fuZeRen"  >
          <Input v-model="form.fuZeRen"  style="width:570px"/>
        </FormItem>
        <FormItem label="销售负责人" prop="xiaoShouRen"  >
          <Input v-model="form.xiaoShouRen"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="到店日期" prop="date"  >
          <Input v-model="form.date"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="店名" prop="dianName"  >
          <Input v-model="form.dianName"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="公司体系评分" prop="fen1"  >
          <Rate v-model="form.fen1"  style="width:570px" disabled/>
        </FormItem>
        <FormItem label="当地行业评分" prop="fen2"  >
          <Rate v-model="form.fen2"  style="width:570px" disabled/>
        </FormItem>
        <FormItem label="当天体系评分" prop="fen3"  >
          <Rate v-model="form.fen3"  style="width:570px" disabled/>
        </FormItem>
        <FormItem label="关系" prop="guanXi"  >
          <Input v-model="form.guanXi"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="面积" prop="areaSize"  >
          <Input v-model="form.areaSize"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="位置" prop="weiZhi"  >
          <Input v-model="form.weiZhi"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="定位" prop="dingWei"  >
          <!-- <div id="allmap" style="width:100%;height:500px"> </div> -->
          <Input v-model="form.dingWei"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="纬度" prop="latitude"  >
          <Input v-model="form.latitude"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="经度" prop="longitude"  >
          <Input v-model="form.longitude"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="版本数量" prop="banBenShuLiang"  >
          <Input v-model="form.banBenShuLiang"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="纯净度" prop="cunJingDu"  >
          <Input v-model="form.cunJingDu"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="艾是占比" prop="aiShiZhanBi"  >
          <Input v-model="form.aiShiZhanBi"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="物流信息" prop="wuLiuXinXi"  >
          <Input v-model="form.wuLiuXinXi"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="人员规模" prop="renYuanGuiMo"  >
          <Input v-model="form.renYuanGuiMo"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="经营评估" prop="jinYingPinGu"  >
          <Input v-model="form.jinYingPinGu"  style="width:570px" readonly/>
        </FormItem>
        <FormItem label="照片" prop="imageList"  >
          <Button type="error" @click="lookImage">查看照片</Button>
            
          <!-- {{imageList}} -->
          
          <!-- <Input v-model="form.imageList"  style="width:570px" readonly/> -->
        </FormItem>
        <FormItem label="点评" prop="about"  >
          <Button type="error" @click="dianPing">点评</Button>
        </FormItem>
        <Row>
          <div v-for="(item,index) in dianPingList" :key="index">
            <Card>
              <p slot="title">{{item.username}}({{item.jobNumber}})</p>
              <p>{{item.content}}</p>
              <Button type="error"  @click="deleteDianPing(item)">删除</Button>
            </Card>
          </div>
        </Row>
          <!-- <Input v-model="form.about"  style="width:570px" readonly/> -->
        
        <Drawer title="到店照片" width="1000px" closable v-model="showDianImage">
          <img v-for="item in imageList" :key="item.id"  mode="aspectFit" style="height: 400px;width: 90%;" :src="item.id">
            <!-- {{imageUrl}} - {{item.id}} - {{item.hou}} -->
          </img>
        </Drawer>
        <!-- <Form-item class="br">
          <Button
            @click="handleSubmit"
            :loading="submitLoading"
            type="primary"
          >提交并保存</Button>
          <Button @click="handleReset">重置</Button>
          <Button type="dashed" @click="close">关闭</Button>
        </Form-item> -->
      </Form>
    </Card>
    <Modal
        v-model="isShowDianPing"
        title="点评到店"
        @on-ok="addDianPing"
        @on-cancel="closeDianPing">
        <div>
          <span>请输入点评内容!</span>
          <input v-model="dianPingNeiRong" />
        </div>
    </Modal>
  </div>
</template>
<script type="text/javascript" :src="hahaha">
</script>

<script>
import  Cookies  from  "js-cookie";
// 根据你的实际请求api.js位置路径修改
import { editDaoDianMessage,getByDaoId,addDianPing,getDianPingByDianId,deleteDianPing } from "./api.js";
export default {
  name: "edit",
  components: {
    },
  props: {
    data: Object
  },
  data() {
    return {
      showDianImage:false,
      daodianId:'',
      dianPingList:[],
      isShowDianPing:false,
      dianPingNeiRong:'',
      imageUrl:'https://artskyhome.com:8082/bs/yihan/',
      imageList: [],
      hahaha:'http://api.map.baidu.com/api?v=3.0&ak=859d16285fd000feec89e9032513f8bb',
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        fuZeRen: "",
        xiaoShouRen: "",
        date: "",
        dianName: "",
        guanXi: "",
        areaSize: "",
        weiZhi: "",
        dingWei: "",
        latitude: "",
        longitude: "",
        banBenShuLiang: "",
        cunJingDu: "",
        aiShiZhanBi: "",
        wuLiuXinXi: "",
        renYuanGuiMo: "",
        jinYingPinGu: "",
        fen1: "",
        fen2: "",
        fen3: "",
        about: "",
        imageList: "",
      },
    // 表单验证规则
    formValidate: {
    }
    };
  },
  watch: {
    form(val) {
      var that = this;
      console.log("watch");
      // console.log(val);
      getByDaoId({daoId:val.id}).then(res =>{
        // console.log(res);
        that.imageList = res.result;
        for(var i = 0 ; i < that.imageList.length ; i ++){
          that.imageList[i].id = "https://artskyhome.com:8082/bs/yihan/" + that.imageList[i].id + "." + that.imageList[i].hou;
        }
        console.log(that.imageList);
      }),
      that.daodianId = val.id;
      getDianPingByDianId({id:val.id}).then(res => {
        // console.log(res);
        that.dianPingList = res.result;
      })
    }
  },
  methods: {
    init() {
      this.handleReset();
      this.form = this.data;
    },
    lookImage(){
      this.showDianImage = true;
    },
    deleteDianPing(e){
      console.log(e);
      let user = JSON.parse(Cookies.get("userInfo"));
      if(e.jobNumber != user.username){
        this.$Message.error();("只能删除自己的点评");
        return ;
      }
      else{
        var that = this;
        this.$Modal.confirm({
          title: "确认删除",
          content: "您确认要删除点评吗?",
          loading: true,
          onOk: () => {
            deleteDianPing({id:e.id}).then( res => {
              this.$Modal.remove();
              getDianPingByDianId({id:that.daodianId}).then(res => {
                that.dianPingList = res.result;
              })
            })
          }
        });
      }
      
    },
    dianPing(){
      this.isShowDianPing = true;
    },
    addDianPing(){
      var that = this;
      // console.log(this.dianPingNeiRong);
      let user = JSON.parse(Cookies.get("userInfo"));
      // console.log(user);
      addDianPing({jobNumber:user.username,username:user.nickname,daoId:that.form.id,content:that.dianPingNeiRong}).then(res =>{
        // console.log(res);
        getDianPingByDianId({id:that.daodianId}).then(res => {
        // console.log(res);
          that.dianPingList = res.result;
        })
      })
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editDaoDianMessage(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("操作成功");
              this.submited();
            }
          });
        }
      });
    },
    close() {
      this.$emit("close", true);
    },
    submited() {
      this.$emit("submited", true);
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style lang="less">
// 建议引入通用样式 具体路径自行修改 可删除下面样式代码
// @import "../../../styles/single-common.less";
.edit-head {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: relative;

    .back-title {
        color: #515a6e;
        display: flex;
        align-items: center;
    }

    .head-name {
        display: inline-block;
        height: 20px;
        line-height: 20px;
        font-size: 16px;
        color: #17233d;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .window-close {
        z-index: 1;
        font-size: 12px;
        position: absolute;
        right: 0px;
        top: -5px;
        overflow: hidden;
        cursor: pointer;

        .ivu-icon-ios-close {
            color: #999;
            transition: color .2s ease;
        }
    }

    .window-close .ivu-icon-ios-close:hover {
        color: #444;
    }
}
</style>