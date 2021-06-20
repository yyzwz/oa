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
        <FormItem label="编码" prop="number"  >
            <Input v-model="form.number" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="姓名" prop="name"  >
            <Input v-model="form.name" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="电话" prop="mobile"  >
            <Input v-model="form.mobile" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="出生日期" prop="birthday"  >
            <DatePicker size="large" type="date" placeholder="请选择日期" @on-change="changeBirthday"></DatePicker>
            <!-- <Input v-model="form.birthday" clearable style="width:570px"/> -->
          </FormItem>
          <FormItem label="性别" prop="sex"  >
            <!-- <Input v-model="form.sex" clearable style="width:570px"/> -->
            <Select v-model="form.sex" clearable style="width:570px">
              <Option
                v-for="(item, i) in this.$store.state.dict.sex"
                :key="i"
                :value="item.value"
              >{{item.title}}</Option>
            </Select>
          </FormItem>
          <FormItem label="学校" prop="school"  >
            <Input v-model="form.school" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="学历" prop="schoolLevel"  >
            <Input v-model="form.schoolLevel" clearable style="width:570px"/>
          </FormItem>
          <FormItem label="专业" prop="major"  >
            <Input v-model="form.major" clearable style="width:570px"/>
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
import { addSonCompany } from "./api3.js";
export default {
  name: "add",
  components: {
    },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        number: "",
        title: "",
        dingId: "",
        ban: true,
        remark: "",
      },
      // 表单验证规则
      formValidate: {
        // number: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  methods: {
    init() {
      this.form.ban = "正常";
    },
    changeBirthday(e){
      var AgoTime = new Date(e);
      this.form.birthday = AgoTime.getFullYear() + '-' + (AgoTime.getMonth() + 1) + '-' + AgoTime.getDate();
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          addSonCompany(this.form).then(res => {
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