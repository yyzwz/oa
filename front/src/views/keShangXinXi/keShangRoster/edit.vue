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
      <Form ref="form" :model="form" :label-width="100" :rules="formValidate" label-position="left" >        <FormItem label="客商编码" prop="shangNumber"  >
          <Input v-model="form.shangNumber" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="客商名称" prop="shangName"  >
          <Input v-model="form.shangName" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="客商类型" prop="shangType"  >
          <Input v-model="form.shangType" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="区域" prop="area"  >
          <Input v-model="form.area" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="省份" prop="sheng"  >
          <Input v-model="form.sheng" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="地级市" prop="city"  >
          <Input v-model="form.city" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="县" prop="xian"  >
          <Input v-model="form.xian" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="街道" prop="jieDao"  >
          <Input v-model="form.jieDao" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="具体地址" prop="address"  >
          <Input v-model="form.address" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="开户行" prop="openHang"  >
          <Input v-model="form.openHang" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="银行账户" prop="hangNumber"  >
          <Input v-model="form.hangNumber" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="信用额度" prop="xinMoney"  >
          <Input v-model="form.xinMoney" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="是否封存" prop="ban"  >
          <i-switch v-model="form.ban"></i-switch>
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
import { editKeShangRoster } from "./api.js";
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
      ban: true,
      remark: "",
    },
    // 表单验证规则
    formValidate: {
    }
    };
  },
  methods: {
    init() {
      this.handleReset();
      this.form = this.data;
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editKeShangRoster(this.form).then(res => {
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