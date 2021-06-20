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
        <FormItem label="宿舍门牌号">
          <Input v-model="form.houseNumber" clearable style="width:570px" readonly/>
          <houseChoose @on-change="getOneHouseData"></houseChoose>
        </FormItem>
        <FormItem label="用户姓名">
          <Input v-model="form.userName" clearable style="width:570px" readonly/>
          <userChoose @on-change="getOneUserData"></userChoose>
        </FormItem>
        <FormItem label="入住时间" prop="checkTime">
          <DatePicker type="date" v-model="form.checkTime" @on-change="changeCheckTime" clearable style="width:570px"></DatePicker>
        </FormItem>
        <FormItem label="备注" prop="remarks"  >
          <Input v-model="form.remarks" clearable style="width:570px"/>
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
import { addDormitoryLease } from "./api.js";
import userChoose from './userChoose.vue';
import houseChoose from './houseChoose.vue';
export default {
  name: "add",
  components: {
    userChoose,
    houseChoose
  },
  data() {
    return {
      submitLoading: false, // 表单提交状态
      form: { // 添加或编辑表单对象初始化数据
        houseNumber: "",
        userName: "",
        checkTime: "",
        rentState: true,
        remarks: "",
        userId:'',
      },
      // 表单验证规则
      formValidate: {
        checkTime: [{ required: true, message: "不能为空", trigger: "blur" }],
        remarks: [{ required: true, message: "不能为空", trigger: "blur" }],
      }
    };
  },
  methods: {
    init() {
    },
    changeCheckTime(){
      var b = new Date(this.form.checkTime);
      this.form.checkTime = b.getFullYear() + '-' + (b.getMonth() + 1) + '-' + b.getDate();
    },
    getOneHouseData(e){
      this.form.houseId = e.id;
      this.form.houseNumber = e.houseNumber;
    },
    getOneUserData(e){
      this.form.userName = e.nickname;
      this.form.userId = e.id;
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if(this.form.userId == ''){
            this.$Message.error('尚未选择员工!');
            return ;
          }
          if(this.form.houseNumber == ''){
            this.$Message.error('尚未选择宿舍!');
            return ;
          }
          this.form.rentState = "在租";
          addDormitoryLease(this.form).then(res => {
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