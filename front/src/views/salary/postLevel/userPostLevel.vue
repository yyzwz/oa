<template>
  <div class="user-edit">
    <!-- {{this.selectDepartmentForm}} -->
    <!-- {{selectPostId}} -->
    <Row>
      <Button @click="openImportUsersByPostWindow" type="info" icon="md-add">导入员工</Button>
      <span style="color:#f00;font-size:24px;">当前选择岗位：{{selectPostName}}</span>
      <Table
        :loading="hualoading"
        border
        :columns="userColumns"
        :data="postUserData"
        sortable="custom"
        @on-sort-change="huachangeSort"
        @on-selection-change="huashowHuaSelect"
        ref="table1"
      ></Table>
      <br>
    </Row>
    <Row type="flex" justify="end" class="page">
      <Page
        :current="huasearchForm.pageNumber"
        :total="huaTotal"
        :page-size="huasearchForm.pageSize"
        @on-change="huachangePage"
        @on-page-size-change="huachangePageSize"
        :page-size-opts="[10,20,50]"
        size="small"
        show-total
        show-elevator
        show-sizer
      ></Page>
    </Row>
    <addEdit :data="form" :type="showType" v-model="showUser" @on-submit="getUserList" />
    <Drawer title="查看当前权限用户" closable v-model="isOpenImportUsersByPostWindow" width="700">
      <Row>
        <Divider>当前角色的用户</Divider>
        <Table
          :loading="hualoading"
          border
          :columns="user1Columns"
          :data="notImportInRoleUserData"
          sortable="custom"
          @on-selection-change="changeNotImportUser"
          ref="table1"
        ></Table>
        <br>
      </Row>
      <Button type="info" @click="importUsersByPost">导入员工</Button>
      <Button type="error" @click="isOpenImportUsersByPostWindow=false">关闭窗口</Button>
    </Drawer>
  </div>
</template>

<script>
import {
  importUserByPostId,
  getNotImportUserData,
  getUserByPostLevel,
  getNotImportUserByDepartmentByPage,
  getAllRoleList,
  addUser, 
  editUser
  } from "./index";
import {
  validateUsername,
  validateMobile,
  validatePassword
} from "@/libs/validate";
import departmentTreeChoose from "@/views/my-components/xboot/department-tree-choose";
import uploadPicInput from "@/views/my-components/xboot/upload-pic-input";
import SetPassword from "@/views/my-components/xboot/set-password";
import addEdit from './addEdit.vue'
export default {
  name: "userPostLevel",
  components: {
    addEdit,
    departmentTreeChoose,
    uploadPicInput,
    SetPassword
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    selectPostId: {
      type:String
    },
    selectPostName: {
      type:String
    }
  },
  data() {
    return {
      isOpenImportUsersByPostWindow:false,
      selectPostName:'',
      showUser: false,
      showType: "0",
      form: {},
      huasearchForm: {
        pageNumber: 1,
        pageSize: 10,
        userName: ""
      },
      selectPostId:'',
      exportHuaData :[],
      selectHuaList :[],
      selectHuaCount :[],
      postUserData:[], //花名册组件的数据
      user1Columns: [
        {
          type: "selection",
          width: 55,
          align: "center",
          fixed: "left"
        },
        {
          title: "工号",
          key: "username",
          width: 220,
          sortable: true
        },
        {
          title: "用户名",
          key: "nickname",
          width: 220,
          sortable: true
        },
        {
          title: "手机号",
          key: "mobile",
          sortable: true,
          sortType: "desc",
          width: 200
        }
      ],
      userColumns: [
        {
          title: "姓名",
          key: "nickname",
          align: "center",
          minWidth: 80,
          sortable: true
        },
        {
          title: "工号",
          key: "username",
          align: "center",
          minWidth: 60,
          sortable: true
        },
        {
          title: "性别",
          key: "sex",
          align: "center",
          minWidth: 50,
          sortable: true
        },
        {
          title: "联系电话",
          key: "mobile",
          align: "center",
          minWidth: 110,
          sortable: true
        },
        {
          title: "岗位",
          key: "postName",
          align: "center",
          minWidth: 100,
          sortable: true
        },
        {
          title: "部门",
          key: "departmentTitle",
          align: "center",
          minWidth: 100
        },
        {
          title: "操作",
          key: "action",
          width: 100,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.edit(params.row);
                    }
                  }
                },
                "编辑"
              )
            ]);
          }
        }
      ],
      visible: this.value,
      title: "",
      passColor: "",
      submitLoading: false,
      maxHeight: 510,
      form: {
        addressArray: []
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
      roleList: [],
      notImportInRoleUserDataTemp:[],
      notImportInRoleUserData:[],
      notImportUserData:[],
    };
  },
  methods: {
    init() {
      this.getRoleList();
      this.getUserByPostLevel();
    },
    importUsersByPost(){
      var that = this;
      if (that.notImportInRoleUserDataTemp.length <= 0) {
        that.$Message.warning("您还未选择要导入的员工");
        return;
      }
      that.$Modal.confirm({
        title: "确认导入",
        content: "您确认要导入所选的 " + that.notImportInRoleUserDataTemp.length + " 位员工?",
        loading: true,
        onOk: () => {
          let ids = "";
          that.notImportInRoleUserDataTemp.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          importUserByPostId({ids: ids,postId:that.selectPostId}).then(res => {
            that.$Modal.remove();
            if (res.success) {
              that.$Message.success("操作成功");
              that.isOpenImportUsersByPostWindow=false;
              that.getUserByPostLevel();
            }
          });
        }
      });
    },
    changeNotImportUser(e){
      this.notImportInRoleUserDataTemp = e;
    },
    openImportUsersByPostWindow(){
      var that = this;
      if (that.selectPostId == -1) {
        that.$Message.warning("您还未选择要要导入的岗位");
        return;
      }
      getNotImportUserData({postId:that.selectPostId}).then(res => {
        console.log(res);
        that.notImportInRoleUserData = res.result;
      })
      that.isOpenImportUsersByPostWindow = true;
    },
    getNotImportUserData(){

    },
    edit(v) {
      // 转换null为""
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.form = data;
      this.showType = "1";
      this.showUser = true;
    },
    getUserByPostLevel(){
      getUserByPostLevel({id:this.selectPostId},this.searchForm).then(res => {
        this.postUserData = res.result.records;
      })
    },
    getRoleList() {
      getAllRoleList().then(res => {
        if (res.success) {
          this.roleList = res.result;
        }
      });
    },
    handleSelectDepTree(v) {
      this.form.departmentId = v;
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
      this.$refs.form.validate(valid => {
        if (valid) {
          if (typeof this.form.birth == "object") {
            this.form.birth = this.format(this.form.birth, "yyyy-MM-dd");
          }

          if (this.type == "1") {
            // 编辑
            this.submitLoading = true;
            editUser(this.form).then(res => {
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
            addUser(this.form).then(res => {
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
    exit(){
      this.$emit("on-submit", true);
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      this.title = "导入花名册员工"; 
      this.visible = value;
      this.getUserByPostLevel();
    }
  },
  watch: {
    value(val) {
      this.setCurrentValue(val);
    },
    visible(value) {
      this.$emit("input", value);
    },
    selectPostId(val){
      this.selectPostId = val;
      this.getUserByPostLevel();
    },
    selectPostName(val){
      this.selectPostName = val;
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

