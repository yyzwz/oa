<template>
  <div>
    <Card>
      <div slot="title">
        <div class="edit-head">
          <a @click="close" class="back-title">
            <Icon type="ios-arrow-back" />返回
          </a>
          <div class="head-name">添加</div>
          <span></span>
          <a @click="close" class="window-close">
            <Icon type="ios-close" size="31" class="ivu-icon-ios-close" />
          </a>
        </div>
      </div>
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >            
        <FormItem label="联系人类型" prop="shangType"  >
            <Select v-model="form.shangType">
              <Option
                v-for="(item, i) in this.$store.state.dict.lianXi"
                :key="i"
                :value="item.value"
              >{{item.title}}</Option>
            </Select>
            <!-- <Input v-model="form.shangType" clearable style="width:570px"/> -->
          </FormItem>
          <FormItem label="联系人姓名" prop="name"  >
            <Input v-model="form.name" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="客商ID" prop="shangId"  >
            <Input v-model="form.shangId" readonly style="width:570px" disabled/>
          </FormItem>
          <FormItem label="手机" prop="shangType"  >
            <!-- <Select placeholder="请选择客商类型" clearable style="width: 200px" v-model="form.shangType">
              <Option
                v-for="(item) in this.shangTypeList"
                :key="item.title"
                :value="item.title"
              >{{item.title}}</Option>
            </Select> -->
            <Input v-model="form.mobile" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="传真" prop="chuanZhen"  >
            <Input v-model="form.chuanZhen" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="联系地址" prop="address"  >
            <Input v-model="form.address" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="是否接受短信" prop="city"  >
            <Input v-model="form.canDuan" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="是否封存" prop="ban"  >
            <i-switch v-model="form.ban" true-value="封存" false-value="正常" ></i-switch>
          </FormItem>
          <FormItem label="备注" prop="remark"  >
            <Input v-model="form.remark" clearable style="width:570px"/>
          </FormItem>
          <Form-item class="br">
            <Button
              @click="handleSubmit"
              :loading="submitLoading"
              type="primary"
            >提交并保存</Button>
            <Button @click="handleReset">重置</Button>
            <Button type="dashed" @click="close">关闭</Button>
          </Form-item>
        </Form>
    </Card>
  </div>
</template>

<script>
// 根据你的实际请求api.js位置路径修改
import { addLook,getAllShengShiXian,getAllShangType } from "./api.js";
export default {
  name: "add",
  components: {
  },
  props:{
    shangId: {
      type: String,
      default: ""
    },
  },
  watch: {
    shangId(val) {
      console.log("addwatch");
      console.log(val);
      if(val != ""){
        this.form.shangId = val;
      }
    }
  },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: {
        shangNumber: "",
        shangName: "",
        shangType: "",
        area: "",
        sheng: "",
        city: "",
        xian: "",
        jieDao: "",
        address: "",
        openHang: "",
        hangNumber: "",
        xinMoney: "",
        ban: "正常",
        remark: "",
      },
      // 表单验证规则
      formValidate: {
      },
      cityData:[],
      cityDataAns:'',
      shangTypeList:[],
    };
  },
  methods: {
    init() {
      this.form.ban = "正常";
      this.getAllShengShiXianFx();
      this.getAllShangTypeFx();
    },
    getAllShangTypeFx(){
      var that = this;
      getAllShangType().then(res => {
        // console.log("getAllShangType");
        // console.log(res);
        that.shangTypeList = res.result;
      })
    },
    changeCityData(e1,e2){
      var that = this;
      // this.form.fenSi = e1[0]; 
      // this.form.bigQu = e1[1];
      this.form.area = e1[2];
      this.form.sheng = e1[3];
      this.form.city = e1[4];
      this.form.xian = e1[5];
    },
    getAllShengShiXianFx(){
      var that = this;
      getAllShengShiXian().then(res => {
        // console.log(res);
        that.cityData = res.result;
      })
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          addLook(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("操作成功");
              this.submited();
              this.form = {
                shangNumber: "",
                shangName: "",
                shangType: "",
                area: "",
                sheng: "",
                city: "",
                xian: "",
                jieDao: "",
                address: "",
                openHang: "",
                hangNumber: "",
                xinMoney: "",
                ban: "正常",
                remark: "",
              };
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