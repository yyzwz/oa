<style lang="less" scoped>
@import "./own-space.less";
</style>

<template>
  <div class="own-space">
    <Card class="own-space-new">
      <div class="own-wrap">
        <div style="width:240px">
          <Menu :active-name="activeName" theme="light" @on-select="changeMenu">
            <MenuItem name="基本信息">基本信息</MenuItem>
            <MenuItem name="安全设置">安全设置</MenuItem>
          </Menu>
        </div>
        <div style="padding: 8px 40px;width:100%">
          <div class="title">{{currMenu}}</div>
          <div>
            <div v-show="currMenu=='基本信息'">
              <Form ref="userForm" :model="userForm" :label-width="90" label-position="left">
                <FormItem label="登录账号：" prop="username">{{userForm.username}}</FormItem>
                <FormItem label="用户头像：">
                  <upload-pic-thumb v-model="userForm.avatar" :multiple="false"></upload-pic-thumb>
                </FormItem>
                <FormItem label="用户名：" prop="nickname">
                  <Input v-model="userForm.nickname" style="width: 300px" />
                </FormItem>
                <FormItem label="性别：">
                  <RadioGroup v-model="userForm.sex">
                    <Radio v-for="(item, i) in dictSex" :key="i" :label="item.value">{{item.title}}</Radio>
                  </RadioGroup>
                </FormItem>
                <FormItem label="生日：">
                  <DatePicker
                    v-model="userForm.birth"
                    @on-change="changeBirth"
                    style="width: 300px"
                    type="date"
                  ></DatePicker>
                </FormItem>
                <FormItem label="所在省市：">
                  <al-cascader
                    v-model="userForm.addressArray"
                    @on-change="changeAddress"
                    data-type="code"
                    level="2"
                    style="width: 300px"
                  />
                </FormItem>
                <FormItem label="街道地址：" prop="street">
                  <Input v-model="userForm.street" style="width: 300px" />
                </FormItem>
                <FormItem label="个人简介：" prop="description">
                  <Input
                    v-model="userForm.description"
                    type="textarea"
                    style="width: 300px"
                    :autosize="{minRows: 3,maxRows: 5}"
                    placeholder="个人简介"
                  ></Input>
                </FormItem>
                <FormItem label="所属部门：">
                  <span>{{ userForm.departmentTitle }}</span>
                </FormItem>
                <FormItem label="用户类型：">
                  <span>{{ userForm.typeTxt }}</span>
                </FormItem>
                <FormItem>
                  <Button
                    type="primary"
                    style="width: 100px;margin-right:5px"
                    :loading="saveLoading"
                    @click="saveEdit"
                  >保存</Button>
                </FormItem>
              </Form>
            </div>
            <div v-show="currMenu=='安全设置'" class="safe">
              <div class="item">
                <div>
                  <div class="title">账户密码</div>
                  <div v-if="userForm.passStrength" class="desc">
                    当前密码强度：
                    <span
                      v-if="userForm.passStrength=='弱'"
                      class="red"
                    >{{userForm.passStrength}}</span>
                    <span v-if="userForm.passStrength=='中'" class="middle">{{userForm.passStrength}}</span>
                    <span v-if="userForm.passStrength=='强'" class="green">{{userForm.passStrength}}</span>
                  </div>
                </div>
                <div>
                  <a @click="changePass">修改</a>
                </div>
              </div>
              <div class="item">
                <div>
                  <div class="title">绑定手机</div>
                  <div class="desc">
                    <span v-if="userForm.mobile">已绑定手机：{{userForm.mobile}}</span>
                    <span v-else>未绑定手机号</span>
                  </div>
                </div>
                <div>
                  <a @click="showChangeMobile">修改</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Card>

    <Modal
      title="修改手机号"
      v-model="editMobileVisible"
      :closable="false"
      :mask-closable="false"
      :width="500"
    >
      <Form
        ref="mobileEditForm"
        :model="mobileEditForm"
        :label-width="70"
        :rules="mobileEditValidate"
      >
        <FormItem label="新手机" prop="mobile">
          <Input v-model="mobileEditForm.mobile" @on-change="hasChangePhone" placeholder="请输入新手机号" />
        </FormItem>
        <FormItem label="验证码" prop="code" :error="codeError">
          <Row type="flex" justify="space-between">
            <Input v-model="mobileEditForm.code" placeholder="请输入您收到的短信验证码" style="width:280px;" />
            <CountDownButton
              ref="countDownMobile"
              @on-click="sendEditMobileCode"
              :disabled="canSendMobileCode"
              :autoCountDown="false"
              :loading="sending"
              :text="getSms"
            />
          </Row>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelInputCodeBox">取消</Button>
        <Button type="primary" :loading="checkCodeLoading" @click="submitEditMobile">提交</Button>
      </div>
    </Modal>

    <check-password ref="checkPassMobile" @on-success="checkSuccessMobile" />

    <check-password ref="checkPassEmail" @on-success="checkSuccessEmail" />
  </div>
</template>

<script>
import {
  userInfo,
  userInfoEdit,
  relatedInfo,
  unRelate,
  sendEditEmail,
  editEmail,
  sendLoginTxSms,
  changeMobile
} from "@/api/index";
import { validateMobile } from "@/libs/validate";
import CountDownButton from "@/views/my-components/xboot/count-down-button";
import uploadPicThumb from "@/views/my-components/xboot/upload-pic-thumb";
import checkPassword from "@/views/my-components/xboot/check-password";
import Cookies from "js-cookie";
import messageVue from "../message/message.vue";
export default {
  components: {
    uploadPicThumb,
    checkPassword,
    CountDownButton
  },
  name: "ownspace_index",
  data() {
    return {
      activeName: "基本信息",
      area: 86,
      messageOpen: 1,
      userForm: {
        id: "",
        avatar: "",
        username: "",
        sex: "",
        mobile: "",
        email: "",
        status: "",
        type: "",
        typeTxt: "",
        address: "",
        addressArray: []
      },
      mobileEditForm: {
        mobile: "",
        code: ""
      },
      emailEditForm: {
        email: "",
        code: ""
      },
      codeError: "",
      initPhone: "",
      initEmail: "",
      saveLoading: false,
      sending: false,
      getSms: "获取验证码",
      canSendMobileCode: true, // 是否可点获取验证码
      checkCodeLoading: false,
      checkPassLoading: false,
      editEmailLoading: false,
      mobileEditValidate: {
        mobile: [
          { required: true, message: "请输入手机号码" },
          { validator: validateMobile }
        ]
      },
      editMobileVisible: false, // 显示填写验证码box
      editEmailVisible: false,
      canSendEditEmail: true,
      relate: {},
      jumping: false,
      currMenu: "基本信息",
      dictSex: this.$store.state.dict.sex
    };
  },
  methods: {
    init() {
      let v = JSON.parse(Cookies.get("userInfo"));
      // 转换null为""
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let userInfo = JSON.parse(str);
      userInfo.addressArray = [];
      this.userForm = userInfo;
      this.initPhone = userInfo.mobile;
      this.mobileEditForm.mobile = userInfo.mobile;
      this.initEmail = userInfo.email;
      this.emailEditForm.email = userInfo.email;
      if (userInfo.address) {
        this.userForm.address = userInfo.address;
        this.userForm.addressArray = JSON.parse(userInfo.address);
      }
      if (this.userForm.type == 0) {
        this.userForm.typeTxt = "普通用户";
      } else if (this.userForm.type == 1) {
        this.userForm.typeTxt = "管理员";
      }
    },
    changePass() {
      this.$router.push({
        name: "change_pass"
      });
    },
    changeMenu(v) {
      this.currMenu = v;
    },
    showChangeMobile() {
      this.$refs.checkPassMobile.show();
    },
    // 密码验证通过 可以修改手机号
    checkSuccessMobile() {
      this.editMobileVisible = true;
      this.canSendMobileCode = false;
    },
    showChangeEmail() {
      this.$refs.checkPassEmail.show();
    },
    checkSuccessEmail() {
      this.editEmailVisible = true;
    },
    saveEdit() {
      this.saveLoading = true;
      if (typeof this.userForm.birth == "object") {
        this.userForm.birth = this.format(this.userForm.birth, "yyyy-MM-dd");
      }
      delete this.userForm.roles;
      userInfoEdit(this.userForm).then(res => {
        this.saveLoading = false;
        if (res.success) {
          this.$Message.success("保存成功");
          // 更新用户信息
          Cookies.set("userInfo", this.userForm);
          // 更新头像
          this.$store.commit("setAvatarPath", this.userForm.avatar);
        }
      });
    },
    changeAddress() {
      this.userForm.address = JSON.stringify(this.userForm.addressArray);
    },
    changeBirth(v) {
      this.userForm.birth = v;
    },
    cancelInputCodeBox() {
      this.editMobileVisible = false;
      this.userForm.mobile = this.initPhone;
    },
    cancelEditEmail() {
      this.editEmailVisible = false;
      this.emailEditForm.email = this.initEmail;
    },
    sendEditMobileCode() {
      var that = this;
      that.canSendMobileCode = true;
      that.$refs["mobileEditForm"].validate(valid => {
        if (valid) {
          that.getSms = "发送中";
          that.sending = true;
          sendLoginTxSms({mobile:that.mobileEditForm.mobile}).then(res => {
            that.getSms = "获取验证码";
            that.sending = false;
            if (res.success) {
              that.$Message.success("发送短信验证码成功");
              that.$refs.countDownMobile.startCountDown();
            }
          });
        }
      });
    },
    submitEditMobile() {
      this.$refs["mobileEditForm"].validate(valid => {
        if (valid) {
          if (!this.mobileEditForm.code) {
            this.codeError = "请填写短信验证码";
            return;
          } else {
            this.codeError = "";
          }
          this.checkCodeLoading = true;
          changeMobile({mobile:this.mobileEditForm.mobile,code:this.mobileEditForm.code}).then(res => {
            this.checkCodeLoading = false;
            console.log(res);
            if (res.success) {
              this.$Message.success("修改成功");
              this.userForm.mobile = this.mobileEditForm.mobile;
              this.initPhone = this.mobileEditForm.mobile;
              this.updateUserInfo();
              this.editMobileVisible = false;
            }
          });
        }
      });
    },
    hasChangePhone() {
      if (this.mobileEditForm.mobile == this.initPhone) {
        this.canSendMobileCode = true;
      } else {
        this.$refs["mobileEditForm"].validate(valid => {
          if (valid) {
            this.canSendMobileCode = false;
          } else {
            this.canSendMobileCode = true;
          }
        });
      }
    },
    hasChangeEmail() {
      if (this.emailEditForm.email == this.initEmail) {
        this.canSendEditEmail = true;
      } else {
        this.canSendEditEmail = false;
      }
    },
    sendVerifyEmail() {
      this.$refs["emailEditForm"].validate(valid => {
        if (valid) {
          this.getSms = "发送中";
          this.sending = true;
          sendEditEmail(this.emailEditForm.email).then(res => {
            this.getSms = "获取验证码";
            this.sending = false;
            if (res.success) {
              this.$Message.success("发送邮件验证码成功，请注意查收");
              // 开始倒计时
              this.$refs.countDownEmail.startCountDown();
            }
          });
        }
      });
    },
    submitEditEmail() {
      this.$refs["emailEditForm"].validate(valid => {
        if (valid) {
          if (!this.emailEditForm.code) {
            this.codeError = "验证码不能为空";
            return;
          } else {
            this.codeError = "";
          }
          this.editEmailLoading = true;
          editEmail(this.emailEditForm).then(res => {
            this.editEmailLoading = false;
            if (res.success) {
              this.initEmail = this.emailEditForm.email;
              this.userForm.email = this.emailEditForm.email;
              this.updateUserInfo();
              this.$Message.success("修改邮件成功");
              this.editEmailVisible = false;
            }
          });
        }
      });
    },
    updateUserInfo() {
      // 更新用户信息
      userInfo().then(res => {
        if (res.success) {
          // 避免超过大小限制
          delete res.result.permissions;
          if (this.getStore("saveLogin")) {
            // 保存7天
            Cookies.set("userInfo", JSON.stringify(res.result), {
              expires: 7
            });
          } else {
            Cookies.set("userInfo", JSON.stringify(res.result));
          }
          this.setStore("userInfo", res.result);
        }
      });
    },
    changeMessage(v) {
      this.setStore("messageOpen", v);
    }
  },
  mounted() {
    let type = this.$route.query.type;
    if (type == "social") {
      this.activeName = "第三方账号绑定";
      this.currMenu = "第三方账号绑定";
    }
    this.init();
  }
};
</script>
