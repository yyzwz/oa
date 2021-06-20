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
        <FormItem label="所在地">
          <Cascader :data="cityData" @on-change="changeCityData" v-model="cityDataAns" :placeholder="tempCity" style="width:570px"></Cascader>  
        </FormItem> 
        <FormItem label="法人" prop="faPeople"  >
          <Input v-model="form.faPeople" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="详细地址" prop="address"  >
          <Input v-model="form.address" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="负责人" prop="fuZe"  >
          <Input v-model="form.fuZe" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="店名" prop="dianName"  >
          <Input v-model="form.dianName" clearable style="width:570px"/>
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
import { editDianMian,getAllShengShiXian } from "./api.js";
export default {
  name: "edit",
  components: {
    },
  props: {
    data: Object
  },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
      xian: "",
      faPeople: "",
      address: "",
      fuZe: "",
      dianName: "",
    },
      // 表单验证规则
      formValidate: {
      },
      cityData:[],
      cityDataAns:'',
      tempCity:'',
    };
  },
  methods: {
    init() {
      this.handleReset();
      this.form = this.data;
      this.getAllShengShiXianFx();
      this.tempCity = this.form.sonCompany + " / " +this.form.bigArea + " / " +this.form.area + " / " +this.form.sheng + " / " +this.form.city + " / " +this.form.xian;
    },
    changeCityData(e1,e2){
      var that = this;
      // console.log(e1); // 简略数组
      // console.log(e2); // 完整数组
      this.form.sonCompany = e1[0]; 
      this.form.bigArea = e1[1];
      this.form.area = e1[2];
      this.form.sheng = e1[3];
      this.form.city = e1[4];
      this.form.xian = e1[5];
    },
    getAllShengShiXianFx(){
      var that = this;
      getAllShengShiXian().then(res => {
        console.log(res);
        that.cityData = res.result;
      })
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editDianMian(this.form).then(res => {
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