<template>
  <div>
    <Card>
      <!-- {{this.form}} -->
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
        <FormItem label="编码" prop="number"  >
          <Input v-model="form.number" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="区域" prop="title"  >
          <Input v-model="form.title" clearable style="width:570px"/>
        </FormItem>
        <FormItem label="分公司" prop="sonCompany"  >
            <Select :placeholder="form.sonCompany" clearable style="width: 200px" @on-change="changeSonCompany">
              <Option
                v-for="(item) in this.sonCompanyList"
                :key="item.title"
                :value="item"
              >{{item.title}}</Option>
            </Select>
            <!-- <Input v-model="form.sonCompany" clearable style="width:570px"/> -->
          </FormItem>
          <FormItem label="大区" prop="bigArea"  >
            <Select :placeholder="form.bigArea" clearable style="width: 200px"  @on-change="changeBigArea">
              <Option
                v-for="(item) in this.bigAreaList"
                :key="item.title"
                :value="item"
              >{{item.title}}</Option>
            </Select>
            <!-- <Input v-model="form.bigArea" clearable style="width:570px"/> -->
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
import { editAreaArchives ,getSonCompany,getByFenGongSi} from "./api.js";
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
      number: "",
      title: "",
      sonCompany: "",
      bigArea: "",
      ban: true,
      remark: "",
    },
    // 表单验证规则
    formValidate: {
    },
      bigAreaList:[],
      sonCompanyList:[],
    };
  },
  methods: {
    init() {
      this.handleReset();
      this.form = this.data;this.getAllSonCompany();
      this.bigAreaList = [{title:'请先选择分公司'}]
    },
    changeBigArea(e){
      var that = this;
      that.form.bigArea = e.title;
    },
    changeSonCompany(e){
      var that = this;
      // console.log(e);
      this.form.sonCompany = e.title;
      // 获取大区
      getByFenGongSi({title:e.title}).then(res =>{
        that.bigAreaList = res.result;
      })
    },
    getAllSonCompany(){
      var that = this;
      getSonCompany().then(res =>{
        // console.log(res);
        that.sonCompanyList = res.result;
      })
    },
    handleReset() {
      this.$refs.form.resetFields();
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          editAreaArchives(this.form).then(res => {
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