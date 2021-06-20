<template>
  <div class="user-edit">
    <Drawer title="导入花名册数据" closable v-model="visible" width="1000">
      <car>
        <Row>
          <Form ref="huasearchForm" :model="huasearchForm" inline :label-width="70">
            <Form-item label="姓名">
              <Input
                type="text"
                v-model="huasearchForm.userName"
                clearable
                placeholder="请输入姓名"
                style="width: 200px"
              />
            </Form-item>
            <Form-item>
              <Button @click="huahandleSearch" type="primary" icon="ios-search">搜索</Button>
              <Button @click="huahandleReset">重置</Button>
            </Form-item>
          </Form>
        </Row>
        <Row v-show="openTip">
          <Alert show-icon>
            已选择
            <span class="select-count">{{huaselectCount}}</span> 项
            <a class="select-clear" @click="huaclearSelectAll">清空</a>
          </Alert>
        </Row>
        <Row>
          <Table
            :loading="hualoading"
            border
            :columns="userColumns"
            :data="huaData"
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
        <Button type="primary" @click="huaAddData">从花名册导入</Button> &nbsp;
        <Button @click="exit">退出</Button>
      </car>
    </Drawer>
  </div>
</template>

<script>
import {
  importFromRoster,
  getNotImportByPage,
  getAllRoleList,
  addUser, 
  editUser
  } from "@/api/index";
import {
  validateUsername,
  validateMobile,
  validatePassword
} from "@/libs/validate";
import departmentTreeChoose from "@/views/my-components/xboot/department-tree-choose";
import uploadPicInput from "@/views/my-components/xboot/upload-pic-input";
import SetPassword from "@/views/my-components/xboot/set-password";
export default {
  name: "user",
  components: {
    departmentTreeChoose,
    uploadPicInput,
    SetPassword
  },
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      huasearchForm: {
        pageNumber: 1,
        pageSize: 10,
        userName: ""
      },
      exportHuaData :[],
      selectHuaList :[],
      selectHuaCount :[],
      huaData:[], //花名册组件的数据
      userColumns: [
        {
          type: "selection",
          minWidth: 55,
          align: "center",
          fixed: "left"
        },
        {
          title: "工号",
          key: "jobNumber",
          minWidth: 100,
          sortable: true
        },
        {
          title: "姓名",
          key: "userName",
          minWidth: 100,
          sortable: true
        },
        {
          title: "联系电话",
          key: "mobile",
          minWidth: 140,
          sortable: true
        },
        {
          title: "身份证号码",
          key: "idCard",
          minWidth: 140,
          sortable: true
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          minWidth: 200
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
      roleList: []
    };
  },
  methods: {
    init() {
      this.getRoleList();
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
    huaAddData() {
      if (this.huaselectCount <= 0) {
        this.$Message.warning("您还未选择要从花名册导入的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认导入",
        content: "您确认要导入所选的 " + this.huaselectCount + " 条数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.huaselectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          importFromRoster({ids:ids}).then(res => {
            this.$Modal.remove();
            if(res.success){
              this.$Message.success("导入成功");
              this.huaclearSelectAll();
              this.getHuaUserList();
              this.$emit("on-submit", true);
            }
          })
        }
      });
    },
    getHuaUserList() {
      this.hualoading = true;
      getNotImportByPage(this.huasearchForm).then(res => { 
        this.hualoading = false;
        if (res.success) {
          if (!this.getStore("roles").includes("ROLE_ADMIN")) {
            res.result.content.forEach(e => {
              e.mobile = "您无权查看该数据";
            });
          }
          this.huaData = res.result.records;
          this.huaTotal = res.result.total;
        }
      });
    },
    huachangePage(v) {
      this.huasearchForm.pageNumber = v;
      this.getHuaUserList();
      this.huaclearSelectAll();
    },
    huachangePageSize(v) {
      this.huasearchForm.pageSize = v;
      this.getHuaUserList();
    },
    huahandleSearch() {
      this.huasearchForm.pageNumber = 1;
      this.huasearchForm.pageSize = 10;
      this.getHuaUserList();
    },
    huahandleReset() {
      // this.$refs.huasearchForm.resetFields();
      this.huasearchForm.userName = '';
      this.huasearchForm.pageNumber = 1;
      this.huasearchForm.pageSize = 10;
      this.getHuaUserList();
    },
    huachangeSort(e) {
      this.huasearchForm.sort = e.key;
      this.huasearchForm.order = e.order;
      if (e.order == "normal") {
        this.searchForm.order = "";
      }
      this.getHuaUserList();
    },
    huashowHuaSelect(e) {
      this.huaselectList = e;
      this.huaselectCount = e.length;
    },
    huaclearSelectAll() {
      this.$refs.table1.selectAll(false);
    },
    setCurrentValue(value) {
      if (value === this.visible) {
        return;
      }
      this.title = "导入花名册员工"; 
      this.visible = value;
      this.getHuaUserList();
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

