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
              <FormItem label="部门名">
                <Input v-model="form.title" maxlength="25" show-word-limit/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="部门代码">
                <Input v-model="form.code" maxlength="100" show-word-limit/>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="部门职责">
                <Input v-model="form.duty" maxlength="100" show-word-limit/>
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
import {editArchive } from "./index";



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
        // 表单验证规则
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
      this.submitLoading = true;
        editArchive(this.form).then(res => {
        this.submitLoading = false;
        if (res.success) {
          this.$Message.success("操作成功");
          this.$emit("on-submit", true);
          this.visible = false;
        }
      });
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      this.title = "部门档案";
      this.maxHeight = "100%"
      this.$refs.departmentArchiveform.resetFields();
      if (this.type == "0" || this.type == "1") {
        let data = this.data;
        this.form = data;
      } else {
        this.form = {};
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

