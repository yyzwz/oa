<template>
  <div class="departmentArchive-edit">
    <Drawer :title="title" v-model="visible" width="720" draggable :mask-closable="type=='0'">
      <div :style="{maxHeight: maxHeight}" class="drawer-content">
        <div class="departmentArchive-title">
          <div class="info-title">基本信息</div>
          <Avatar :src="form.avatar" size="large" v-show="type!='2'" />
        </div>
        <Form ref="departmentArchiveform" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32">
            <Col span="12">
              <FormItem label="部门名" prop="title">
                <Input v-model="form.title" />
              </FormItem>
            </Col>
            <Col span="12">
                <FormItem label="钉钉部门编码(只读)">
                <Input v-model="form.dingDingCode" readonly />
              </FormItem>
            </Col>
          </Row>
          <row>
               <Button type="primary" :loading="submitLoading" @click="submit">提交</Button>
                <Button @click="visible = false">取消</Button>
          </row>
        </Form>
      </div>
      
    </Drawer>
  </div>
</template>

<script>
import {addDepartmentArchive, editDepartmentArchive } from "./api.js";
export default {
  name: "departmentArchive",
  props: {
    value: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object
    },
    type: {
      type: String,
      default: "0"
    }
  },
  data() {
    return {
      visible: this.value,
      title: "",
      passColor: "",
      submitLoading: false,
      maxHeight: 510,
      form: {
        sortOrder: 0,
        status: 0
      },
      formValidate: {
        title: [
          { required: true, message: "请输入部门名", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    init() {
    },
    submit() {
      this.$refs.departmentArchiveform.validate(valid => {
        if (valid) {
          // 添加
           if (this.type == "1") {
            // 编辑
            this.submitLoading = true;
             editDepartmentArchive(this.form).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("操作成功");
                this.$emit("on-submit", true);
                this.visible = false;
              }
            });
          } else {
            // 添加
            this.submitLoading = true;
            addDepartmentArchive(this.form).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("操作成功");
                this.$emit("on-submit", true);
                this.visible = false;
              }
            });
          }
        }
      });
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      if (this.type == "1") {
        this.title = "编辑部门";
        this.maxHeight = Number(document.documentElement.clientHeight - 121) + "px";
      } else if (this.type == "2") {
        this.title = "添加部门";
        this.maxHeight = Number(document.documentElement.clientHeight - 121) + "px";
      } else {
        this.title = "部门详情";
        this.maxHeight = "100%";
      }
      // 清空数据
      this.$refs.departmentArchiveform.resetFields();
      if (this.type == "0" || this.type == "1") {
        // 回显数据
        let data = this.data;
        // 回显
        this.form = data;
      } else {
        // 添加
      }
      this.visible = value;
    }
  },
  watch: {
    value(val) {
      this.setCurrentValue(val);
    },
    visible(value) {
      this.$emit("input", value);
    }
  },
  mounted() {
    this.init();
  }
};
</script>

<style lang="less">
@import "../../../styles/table-common.less";
.drawer-content {
  overflow: auto;
}
.drawer-content::-webkit-scrollbar {
  display: none;
}
.departmentArchive-title {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  .info-title {
    font-size: 16px;
    color: rgba(0, 0, 0, 0.85);
    display: block;
    margin-right: 16px;
  }
}
.info-header {
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
  display: block;
  margin-bottom: 16px;
}
</style>

