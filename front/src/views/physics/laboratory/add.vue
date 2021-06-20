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
        <FormItem label="实验室编号" prop="number"  >
            <Input v-model="form.number" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="实验室名称" prop="name"  >
            <Input v-model="form.name" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="分类" prop="type"  >
            <Input v-model="form.type" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="人容量" prop="volume"  >
            <Input v-model="form.volume" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="温度" prop="temperature"  >
            <Input v-model="form.temperature" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="湿度" prop="humidity"  >
            <Input v-model="form.humidity" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="甲醛浓度" prop="formaldehyde"  >
            <Input v-model="form.formaldehyde" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="二氧化碳浓度" prop="carbonDioxide"  >
            <Input v-model="form.carbonDioxide" clearable style="width:570px"/>
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
import { addLaboratoryInformation } from "./api.js";
export default {
  name: "add",
  components: {
    },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        number: "",
        name: "",
        type: "",
        volume: "",
        temperature: "",
        humidity: "",
        formaldehyde: "",
        carbonDioxide: "",
      },
      // 表单验证规则
      formValidate: {
      }
    };
  },
  methods: {
    init() {},
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          addLaboratoryInformation(this.form).then(res => {
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