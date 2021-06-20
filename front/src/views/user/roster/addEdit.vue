<template>
  <div class="user-edit">
    <!-- Drawer抽屉 -->
    <Drawer :title="title" v-model="visible" width="720" draggable :mask-closable="type=='0'">
      <div :style="{maxHeight: maxHeight}" class="drawer-content">
        <div class="user-title">
          <div class="info-title">基本信息</div>
          <Avatar :src="form.avatar" size="large" v-show="type!='2'" />
        </div>
        <Form label-colon v-show="type!='2'">
          <Row :gutter="32">
            <Col span="12">
              <FormItem label="用户ID">
                {{form.id}}
                <Tooltip trigger="hover" placement="right" content="账户已禁用">
                  <Icon
                    v-show="form.status==-1"
                    type="md-lock"
                    size="18"
                    style="margin-left:10px;cursor:pointer"
                  />
                </Tooltip>
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="登录账号">
                {{form.username}}
                <Tooltip trigger="hover" placement="right" :content="`密码强度：${form.passStrength}`">
                  <Icon
                    v-show="form.passStrength"
                    type="md-key"
                    :color="passColor"
                    size="18"
                    style="margin-left:10px;cursor:pointer"
                  />
                </Tooltip>
              </FormItem>
            </Col>
          </Row>
        </Form>
        <Form ref="form" :model="form" :rules="formValidate" label-position="top">
          <Row :gutter="32" v-if="type=='1'" >
            <Col span="12">
              <FormItem label="人员ID">
                <Input v-model="form.id" disabled/>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32" >
            <Col span="12">
              <FormItem label="工号">
                <Input v-model="form.jobNumber" readonly />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="姓名">
                <Input v-model="form.userName"  />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="电话号码">
                <Input v-model="form.mobile" />
              </FormItem>
            </Col>
            <Col span="12">
              <FormItem label="身份证号">
                <Input v-model="form.idCard" @on-change="changeIdcard" maxlength="18" show-word-limit/>
              </FormItem>
            </Col>
          </Row>
          <Row :gutter="32" >
            <Col span="11">
              <FormItem label="是否导入至用户">
                <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                  <span slot="open">导入</span>
                  <span slot="close">不导入</span>
                </i-switch>
              </FormItem>
            </Col>
          </Row>

          <Divider />
          
          <Row :gutter="80" v-show="type!='0'" style="padding-left:10px;">
            <Col span="4">
              <Button type="primary" :loading="submitLoading" @click="submit">提交</Button>
            </Col>
            <Col span="4">
              <Button @click="visible = false" >取消</Button>
            </Col>
          </Row>
        </Form>
      </div>
    </Drawer>
  </div>
</template>

<script>
import { 
  getNewJobNumber,
  getCityByIdcard,
  getAllRoleList, 
  addUser,
  editUser,
  addDingDingDepartment,
  updateDingDept,
  addHuaOnDing,
  editHuaOnDing,
  checkMobile
  } from "./index";
import {
  validateUsername,
  validateMobile,
  validatePassword
} from "@/libs/validate";
export default {
  name: "user",
  components: {
  },
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
        status: 0,
        parentid:'1',
        name:'',
        jobNumber:'',
      },
      formValidate: {
        // 表单验证规则
        username: [
          { required: true, message: "请输入登录账号", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" }
        ],
        nickname: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "请输入手机号", trigger: "blur" },
          { validator: validateMobile, trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePassword, trigger: "blur" }
        ],
        email: [
          { required: true, message: "请输入邮箱地址" },
          { type: "email", message: "邮箱格式不正确" }
        ]
      },
      roleList: []
    };
  },
  methods: {
    init() {
      this.getRoleList();
    },
    getNewJobNumber(){
      var that = this;
      getNewJobNumber().then(res =>{
        var jobNumber = res.result;
        console.log(jobNumber);
        that.form.jobNumber = jobNumber;
      });
    },
    changeIdcard(){
      var idcard = this.form.idCard;
      if(idcard.length == 18){
        // 性别
        if(idcard[16] == '1'){
          this.form.sex = "男";
        }
        else{
          this.form.sex = "女";
        }
        // 出生地
        var cityId = idcard.substring(0,6);
        getCityByIdcard({cityIds: cityId}).then(res =>{
          this.form.nativePlace = res;
        })
        var birthYear = idcard.substring(6,10);
        this.form.age = new Date().getFullYear() - birthYear + "";
        // 生日
        var birthday = idcard.substring(6,10) + "-" 
                    + idcard.substring(10,12) + "-"
                    + idcard.substring(12,14);
        this.form.birthday = birthday;
      }
    },
    getRoleList() {
      getAllRoleList().then(res => {
        if (res.success) {
          this.roleList = res.result;
        }
      });
    },
    handleSelectDepTree(v) {
      this.form.departmentIds = v;
    },
    changeAddress(v) {
      this.form.address = JSON.stringify(this.form.addressArray);
    },
    changePass(v, grade, strength) {
      this.form.passStrength = strength;
    },
    changeBirth(v, d) {
      this.form.birth = v;
    },
    submit() {
      checkMobile({mobile: this.form.mobile}).then(res =>{
        if(res.result == null){
          this.submitLoading = true;
          if(this.form.status == undefined){
            this.form.status = 0;
          }
          addHuaOnDing(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("添加成功");
              this.$emit("on-submit", true);
              this.visible = false;
            }
          });
        }else{
          this.$Message.error("手机号码已存在");
        }
      })      
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      this.title = "添加用户";
      this.$refs.form.resetFields();
      this.form = {
        type: 0,
        jobNumber:'',
        idCard:'',
        mobile:'',
        userName:'',
      };
      this.visible = value;
      this.getNewJobNumber();
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
.user-title {
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

