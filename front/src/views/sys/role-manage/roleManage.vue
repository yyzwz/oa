<style lang="less">
@import "../../../styles/table-common.less";
@import "./roleManage.less";
</style>
<template>
  <div class="search">
   <span class="qmm1">角色组：{{editTitle}}</span>
    <Card>
      <Row class="operation">
        <Button @click="importJueSe" type="primary" icon="md-add">导入</Button>
        <!-- <Button @click="addRole" type="primary" icon="md-add">添加角色</Button> -->
        <Button @click="delAll" icon="md-trash">批量删除</Button>
        <Button @click="init" icon="md-refresh">全部</Button>
        <Button type="dashed" @click="openTip=!openTip">{{openTip ? "关闭提示" : "开启提示"}}</Button>
      </Row>
      <Row v-show="openTip">
        <Alert show-icon>
          已选择
          <span class="select-count">{{selectCount}}</span> 项
          <a class="select-clear" @click="clearSelectAll">清空</a>
        </Alert>
      </Row>
      <Row>
        <Table
          :loading="loading"
          border
          :columns="columns"
          :data="data"
          ref="table"
          sortable="custom"
          @on-sort-change="changeSort"
          @on-selection-change="changeSelect"
        ></Table>
      </Row>
      <Row type="flex" justify="end" class="page">
        <Page
          :current="pageNumber"
          :total="total"
          :page-size="pageSize"
          @on-change="changePage"
          @on-page-size-change="changePageSize"
          :page-size-opts="[10,20,50]"
          size="small"
          show-total
          show-elevator
          show-sizer
        ></Page>
      </Row>
    </Card>
    <Drawer title="导入花名册数据" closable v-model="isImportJueSeData" width="1000">
      <car>
        <Row>
          <Table
            :loading="loading"
            border
            :columns="userColumns"
            :data="huaData"
            sortable="custom"
            @on-sort-change="changeSort"
            @on-selection-change="showHuaSelect"
            ref="table"
          ></Table>
          <br>
          <Button type="primary" @click="huaAddData">从花名册导入</Button> &nbsp;
          <Button @click="isImportJueSeData=false">退出</Button>
          <!-- {{zwzRoleId}} -->
        </Row>
      </car>
    </Drawer>

    <!-- 编辑 -->
    <Modal :title="modalTitle" v-model="roleModalVisible" :mask-closable="false" :width="500">
      <Form ref="roleForm" :model="roleForm" :label-width="80" :rules="roleFormValidate">
        <FormItem label="角色名称" prop="name">
          <Input v-model="roleForm.name" placeholder="按照Spring Security约定建议以‘ROLE_’开头" />
        </FormItem>
        <FormItem label="备注" prop="description">
          <Input v-model="roleForm.description" type="textarea"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelRole">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitRole">提交</Button>
      </div>
    </Modal>
    <!-- 菜单权限 -->
    <Modal
      :title="modalTitle"
      v-model="permModalVisible"
      :mask-closable="false"
      :width="500"
      :styles="{top: '30px'}"
      class="permModal"
      @on-ok="submitPermEdit"
      @on-cancel="cancelPermEdit"
    >
      <Row :gutter="32">
        <Col span="12">
        <div style="position:relative">
          <Tree
            ref="tree"
            :data="permData"
            show-checkbox
            :render="renderContent"
            :check-strictly="true"
          ></Tree>
          <Spin size="large" fix v-if="treeLoading"></Spin>
        </div>
        <div style="position:relative">
          <Button type="text" @click="cancelPermEdit">取消</Button>
          <Select
            v-model="openLevel"
            @on-change="changeOpen"
            style="width:110px;text-align:left;margin-right:10px"
          >
            <Option value="0">展开所有</Option>
            <Option value="1">收合所有</Option>
            <Option value="2">仅展开一级</Option>
            <Option value="3">仅展开两级</Option>
          </Select>
          <Button @click="selectTreeAll">全选/反选</Button>
          <Button type="primary" :loading="submitPermLoading" @click="submitPermEdit">提交</Button>
        </div>
        </Col>
      </Row>
    </Modal>
    <!-- 数据权限 -->
    <Modal
      :title="modalTitle"
      v-model="depModalVisible"
      :mask-closable="false"
      :width="500"
      class="depModal"
    >
      <Alert show-icon>默认可查看全部数据，自定义数据范围时请勾选下方数据</Alert>
      <Form :label-width="85">
        <FormItem label="数据范围">
          <Select v-model="dataType" transfer>
            <Option :value="0">全部数据权限</Option>
            <Option :value="1">自定义数据权限</Option>
            <Option :value="2">本部门及以下数据权限</Option>
            <Option :value="3">本部门数据权限</Option>
          </Select>
        </FormItem>
      </Form>
      <div v-show="dataType==1" style="margin-top:15px">
        <div style="position:relative">
          <Tree
            ref="depTree"
            :data="depData"
            :load-data="loadData"
            @on-toggle-expand="expandCheckDep"
            multiple
            style="margin-top:15px"
          ></Tree>
          <Spin size="large" fix v-if="depTreeLoading"></Spin>
        </div>
      </div>
      <div slot="footer">
        <Button type="text" @click="depModalVisible=false">取消</Button>
        <Button type="primary" :loading="submitDepLoading" @click="submitDepEdit">提交</Button>
      </div>
    </Modal>
    <Drawer title="查看当前权限用户" closable v-model="isImportHuaData1" width="700">
      <car>
        <!-- 123{{userHuaData}} -->
        <Row>
          <Divider>当前角色的用户</Divider>
          <Table
            :loading="hualoading"
            border
            :columns="user1Columns"
            :data="userHuaData"
            sortable="custom"
            @on-sort-change="huachangeSort"
            @on-selection-change="huashowHuaSelect"
            ref="table1"
          ></Table>
          <br>
        </Row>
        <Button type="error" @click="isImportHuaData1=false">关闭</Button>
        <Divider>不是该岗位的员工</Divider>
        <Table
            :loading="hualoading"
            border
            :columns="user1Columns"
            :data="notImportInRoleUserData"
            sortable="custom"
            @on-sort-change="huachangeSort"
            @on-selection-change="notImportInRoleUserDataChange"
            ref="table2"
          ></Table>
          <br>
        <Button type="info" @click="importUsersInRole">导入新用户</Button>
      </car>
    </Drawer>
  </div>
</template>

<script>
import {
  importUserByRoleId,
  findNotImportUserByRoleId,
  getUserListData2,
  importHuaData,
  getAllRoles,
  getRoleList,
  getAllPermissionList,
  addRole,
  editRole,
  deleteRole,
  setDefaultRole,
  editRolePerm,
  initDepartment,
  loadDepartment,
  editRoleDep
} from "./index";
import  Cookies  from  "js-cookie";
import util from "@/libs/util.js";
export default {
  name: "role-manage",
  data() {
    return {
      editTitle:"",
      openTip: true,
      openLevel: "0",
      loading: true,
      treeLoading: true,
      depTreeLoading: true,
      submitPermLoading: false,
      submitDepLoading: false,
      searchKey: "",
      sortColumn: "createTime",
      sortType: "desc",
      modalType: 0,
      roleModalVisible: false,
      permModalVisible: false,
      depModalVisible: false,
      modalTitle: "",
      roleForm: {
        roleGroupId:"-1",
        roleGroupName:"",
        name: "",
        description: ""
      },
      roleFormValidate: {
        name: [{ required: true, message: "角色名称不能为空", trigger: "blur" }]
      },
      submitLoading: false,
      selectList: [],
      selectCount: 0,
      columns: [
        {
          type: "selection",
          width: 60,
          align: "center"
        },
        {
          type: "index",
          width: 60,
          align: "center"
        },
        {
          title: "角色名称",
          key: "title",
          width: 150,
          sortable: true
        },
        {
          title: "职责",
          key: "requirement",
          minWidth: 150,
          sortable: true
        },
        {
          title: "创建时间",
          key: "createTime",
          width: 170,
          sortable: true,
          sortType: "desc"
        },
        {
          title: "更新时间",
          key: "updateTime",
          width: 170,
          sortable: true
        },
        {
          title: "是否设置为注册用户默认角色",
          key: "defaultRole",
          align: "center",
          width: 220,
          render: (h, params) => {
            if (params.row.defaultRole) {
              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      type: "success",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.cancelDefault(params.row);
                      }
                    }
                  },
                  "取消默认"
                )
              ]);
            } else {
              return h("div", [
                h(
                  "Button",
                  {
                    props: {
                      type: "info",
                      size: "small"
                    },
                    style: {
                      marginRight: "5px"
                    },
                    on: {
                      click: () => {
                        this.setDefault(params.row);
                      }
                    }
                  },
                  "设为默认"
                )
              ]);
            }
          }
        },
        {
          title: "操作",
          key: "action",
          align: "center",
          fixed: "right",
          minWidth: 300,
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "warning",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.editPerm(params.row);
                    }
                  }
                },
                "菜单权限"
              ),
               h(
                "Button",
                {
                  props: {
                    type: "success",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.lookUser(params.row);
                    }
                  }
                },
                "查看用户"
              ),
              h(
                "Button",
                {
                  props: {
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
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    size: "small"
                  },
                  on: {
                    click: () => {
                      this.remove(params.row);
                    }
                  }
                },
                "删除"
              )
            ]);
          }
        }
      ],
      data: [],
      pageNumber: 1,
      pageSize: 10,
      total: 0,
      permData: [],
      editRolePermId: "",
      selectAllFlag: false,
      depData: [],
      dataType: 0,
      editDepartments: [],
      isImportJueSeData:false,
      userColumns: [
        {
          type: "selection",
          width: 55,
          align: "center",
          fixed: "left"
        },
        {
          title: "角色名",
          key: "title",
          width: 220,
          sortable: true
        },
        {
          title: "职责",
          key: "requirement",
          width: 220,
          sortable: true
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          width: 200
        }
      ],
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
          title: "创建时间",
          key: "createTime",
          sortable: true,
          sortType: "desc",
          width: 200
        }
      ],
      huaselectCount:0,
      exportHuaData :[],
      selectHuaList :[],
      selectHuaCount :[],
      huasearchForm: {
        pageNumber: 1,
        pageSize: 10,
        username:'',
      },
      // 是否展示花名册导入抽屉
      propForm:[],
      //花名册选择左侧选项时候保存的数据
      exportHuaData :[],
      selectHuaList :[],
      selectHuaCount :[],
      // 是否展示花名册导入抽屉
      isImportHuaData: false,
      huaData:[], //花名册组件的数据
      zwzRoleId:'',
      selectArray:[],
      selectDictTime:[],
      selectArrayFlag: false,
      selectDictTimeInitFlag: false,
      // 右边显示的数据
      rightRolePerms:[],
      roleidzwz:'',
      isImportHuaData1:false,
      notImportInRoleUserData:[],
      notImportInRoleUserDataTemp:[],
    };
  },
  
  props: ['roleGroup'],
  watch:{
      roleGroup:function(newVal,oldVal){
        this.roleForm.roleGroupId=newVal.id;
        this.roleForm.roleGroupName=newVal.title;
        this.editTitle=newVal.title;
        this.getRoleListByRoleGroupId(newVal.id);
        this.zwzRoleId = newVal.id;
      },
      permModalVisible:function(newVal,oldVal){
        if(newVal == false){
          // 关闭时，已有数据初始化
          this.selectArrayFlag = false;

        }
        else if(newVal == true){

        }
      }
  },
  methods: {
    init() {
      this.selectArrayFlag = false;
      this.selectDictTimeInitFlag = false;
      this.getRoleList();
      // 获取所有菜单权限树
      this.getPermList();
      this.editTitle="";
      this.getHuaUserList();
      this.selectDictTimeInit();
    },
    notImportInRoleUserDataChange(e){
      this.notImportInRoleUserDataTemp = e;
    },
    importUsersInRole(){
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
          importUserByRoleId({ids: ids,roleId:that.roleidzwz}).then(res => {
            that.$Modal.remove();
            if (res.success) {
              that.$Message.success("操作成功");
              that.clearSelectAll();
              that.isImportHuaData1=false;
            }
          });
        }
      });
    },
    lookUser(v){
      this.roleidzwz = v.id;
      this.openImportHuaWindows();
    },
    openImportHuaWindows (){
      this.getHuaUserList2();
      
      this.$Modal.remove();
    },
    getHuaUserList2(){
      var that = this;
      
      // this.hualoading = true;
      // console.log(that.roleidzwz);
      getUserListData2({roleid: that.roleidzwz}).then(res => { // getHuaListData
        that.hualoading = false;
          // console.log(res);
        if (res.success) {
          this.isImportHuaData1=true;
          that.userHuaData = res.result;
          // console.log(that.userHuaData);
          that.userhuaTotal = res.result.length;
        }
      });
      findNotImportUserByRoleId({roleId:that.roleidzwz}).then(res => {
        // console.log(res);
        that.notImportInRoleUserData = res.result;
      })
    },
    selectDictTimeInit(){
      if(this.selectDictTimeInitFlag == true) return ;
      this.selectDictTime = this.$store.state.dict.powerLimit;
      this.selectDictTimeInitFlag = true;
    },
    importJueSe(){
      this.isImportJueSeData = true;
    },
    huaAddData() {
      var zwzid = this.zwzRoleId;
      if(this.zwzRoleId == ''){
        this.$Message.warning("您还未选择要导入到哪个角色组");
        return;
      }
      if (this.selectHuaCount <= 0) {
        this.$Message.warning("您还未选择导入的角色");
        return;
      }
      this.$Modal.confirm({
        title: "确认导入",
        content: "您确认要导入所选的 " + this.selectHuaCount + " 条数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectHuaList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          importHuaData({ids:ids,partid:zwzid}).then(res => {
            this.$Modal.remove();
            if(res.success){
              this.$Message.success("导入成功");
              this.clearSelectAll();
              this.init();
              // this.getUserList();
              // this.getHuaUserList();
              this.isImportJueSeData=false;
            }
          })
        }
      });
    },
    getHuaUserList() {
      // 多条件搜索用户列表
      // this.loading = true;
      getAllRoles().then(res => {
        // this.loading = false;
        if (res.success) {
          this.huaData = res.result;
        }
      });
    },
    showHuaSelect(e) {
      this.exportHuaData = e;
      this.selectHuaList = e;
      this.selectHuaCount = e.length;
    },
    renderContent(h, { root, node, data }) {
      let icon = "";
      if (data.level == 0) {
        icon = "ios-navigate";
      } else if (data.level == 1) {
        icon = "md-list-box";
      } else if (data.level == 2) {
        icon = "md-list";
      } else if (data.level == 3) {
        icon = "md-radio-button-on";
      } else {
        icon = "md-radio-button-off";
      }
      return h(
        "span",
        {
          style: {
            display: "inline-block",
            cursor: "pointer"
          },
          on: {
            click: () => {
              data.checked = !data.checked;
            }
          }
        },
        [
          h("span", [
            h("Icon", {
              props: {
                type: icon,
                size: "16"
              },
              style: {
                "margin-right": "8px",
                "margin-bottom": "3px"
              }
            }),
            h("span", data.title)
          ])
        ]
      );
    },
    changePage(v) {
      this.pageNumber = v;
      this.getRoleList();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.pageSize = v;
      this.getRoleList();
    },
    changeSort(e) {
      this.sortColumn = e.key;
      this.sortType = e.order;
      if (e.order == "normal") {
        this.sortType = "";
      }
      this.getRoleList();
    },
    getRoleList() {
      this.loading = true;
      let params = {
        PageVo:{
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          sort: this.sortColumn,
          order: this.sort
        },
        roleGroupId:this.roleForm.roleGroupId
      };
      getRoleList(params).then(res => {
        this.loading = false;
        if (res.success) {
          this.data = res.result.records;
          this.total = res.result.total;
        }
      });
    },
    getRoleListByRoleGroupId(roleGroupId) {
      this.loading = true;
      let params = {
        PageVo:{
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          sort: this.sortColumn,
          order: this.sort
        },
        roleGroupId:roleGroupId
      };
      getRoleList(params).then(res => {
        this.loading = false;
        if (res.success) {
          this.data = res.result.records;
          this.total = res.result.total;
        }
      });
    },
    getPermList() {
      var that = this;
      this.treeLoading = true;
      getAllPermissionList().then(res => {
        if (res.success) {
          this.deleteDisableNode(res.result);
          this.permData = res.result;
          this.treeLoading = false;
          for(var i = 0 ; i< this.permData.length ; i ++){
            if(this.permData[i].duty != null){
              this.permData[i].title = this.permData[i].title + '(' + this.permData[i].duty + ')';
            }
            for(var j = 0 ; j < that.permData[i].children.length ; j ++){
              if(this.permData[i].children[j].duty != null){
                this.permData[i].children[j].title = this.permData[i].children[j].title + '(' + this.permData[i].children[j].duty + ')';
              }
              for(var k = 0 ; k < that.permData[i].children[j].children.length ; k ++){
                if(that.permData[i].children[j].children[k].duty != null){
                  that.permData[i].children[j].children[k].title = that.permData[i].children[j].children[k].title + '(' + that.permData[i].children[j].children[k].duty + ')';
                }
              }
            }
          }
        }
        this.treeLoading = false;
      });
    },
    // 递归标记禁用节点
    deleteDisableNode(permData) {
      let that = this;
      permData.forEach(function(e) {
        if (e.status == -1) {
          e.title = "[已禁用] " + e.title;
          e.disabled = true;
        }
        if (e.children && e.children.length > 0) {
          that.deleteDisableNode(e.children);
        }
      });
    },
    cancelRole() {
      this.roleModalVisible = false;
    },
    submitRole() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          if (this.modalType == 0) {
            // 添加
            this.submitLoading = true;
            addRole(this.roleForm).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("操作成功");
                this.getRoleList();
                this.roleModalVisible = false;
              }
            });
          } else {
            this.submitLoading = true;
            editRole(this.roleForm).then(res => {
              this.submitLoading = false;
              if (res.success) {
                this.$Message.success("操作成功");
                this.getRoleList();
                this.roleModalVisible = false;
              }
            });
          }
        }
      });
    },
    addRole() {
      this.modalType = 0;
      this.modalTitle = "添加角色";
      this.$refs.roleForm.resetFields();
      delete this.roleForm.id;
      this.roleModalVisible = true;
    },
    edit(v) {
      this.modalType = 1;
      this.modalTitle = "编辑角色";
      this.$refs.roleForm.resetFields();
      // 转换null为""
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let roleInfo = JSON.parse(str);
      this.roleForm = roleInfo;
      this.roleModalVisible = true;
    },
    remove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除角色 " + v.name + " ?",
        loading: true,
        onOk: () => {
          deleteRole({ids: v.id}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.getRoleList();
            }
          });
        }
      });
    },
    setDefault(v) {
      this.$Modal.confirm({
        title: "确认设置",
        content: "您确认要设置所选的 " + v.name + " 为注册用户默认角色?",
        loading: true,
        onOk: () => {
          let params = {
            id: v.id,
            isDefault: true
          };
          setDefaultRole(params).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("操作成功");
              this.getRoleList();
            }
          });
        }
      });
    },
    cancelDefault(v) {
      this.$Modal.confirm({
        title: "确认取消",
        content: "您确认要取消所选的 " + v.name + " 角色为默认?",
        loading: true,
        onOk: () => {
          let params = {
            id: v.id,
            isDefault: false
          };
          setDefaultRole(params).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("操作成功");
              this.getRoleList();
            }
          });
        }
      });
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
    },
    changeSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未选择要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          deleteRole({ids: ids}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.clearSelectAll();
              this.getRoleList();
            }
          });
        }
      });
    },
    editPerm(v) {
      this.editRolePermId = v.id;
      this.modalTitle = "分配 " + v.name + " 的菜单权限";
      // 匹配勾选
      let rolePerms = v.permissions;
      if (this.treeLoading) {
        this.$Message.warning("菜单权限数据加载中，请稍后点击查看");
        return;
      }
      // 递归判断子节点
      this.rightRolePerms = rolePerms;
      this.checkPermTree(this.permData, rolePerms);
      this.permModalVisible = true;
    },
    // 递归判断子节点
    checkPermTree(permData, rolePerms) {
      let that = this;
      permData.forEach(function(p) {
        if (that.hasPerm(p, rolePerms) && p.status != -1) {
          p.checked = true;
        } else {
          p.checked = false;
        }
        if (p.children && p.children.length > 0) {
          that.checkPermTree(p.children, rolePerms);
        }
      });
    },
    // 判断角色拥有的权限节点勾选
    hasPerm(p, rolePerms) {
      let flag = false;
      for (let i = 0; i < rolePerms.length; i++) {
        if (p.id == rolePerms[i].permissionId) {
          flag = true;
          break;
        }
      }
      if (flag) {
        return true;
      }
      return false;
    },
    // 全选反选
    selectTreeAll() {
      this.selectAllFlag = !this.selectAllFlag;
      let select = this.selectAllFlag;
      this.selectedTreeAll(this.permData, select);
    },
    // 递归全选节点
    selectedTreeAll(permData, select) {
      let that = this;
      permData.forEach(function(e) {
        e.checked = select;
        if (e.children && e.children.length > 0) {
          that.selectedTreeAll(e.children, select);
        }
      });
    },
    submitPermEdit() {
      var that = this;
      this.submitPermLoading = true;
      let permIds = "";
      let qiXians = "";
      let selectedNodes = this.$refs.tree.getCheckedNodes();
      selectedNodes.forEach(function(e) {
        let qixiana = "999999";
        permIds += e.id + ",";
        for(var i = 0 ; i < that.selectArray.length ; i ++){
          if(e.id == that.selectArray[i].id) {
            qixiana = that.selectArray[i].qiXian;
          }
        }
        qiXians += qixiana + ",";
      });
      var user = JSON.parse(Cookies.get("userInfo"));
      permIds = permIds.substring(0, permIds.length - 1);
      qiXians = qiXians.substring(0, qiXians.length - 1);
      editRolePerm({
        user: user.nickname,
        roleId: this.editRolePermId,
        permIds: permIds,
        userRoleQiXian:qiXians
      }).then(res => {
        this.submitPermLoading = false;
        if (res.success) {
          this.$Message.success("操作成功");
          // 标记重新获取菜单数据
          this.$store.commit("setAdded", false);
          util.initRouter(this);
          this.getRoleList();
          this.permModalVisible = false;
        }
      });
    },
    cancelPermEdit() {
      this.permModalVisible = false;
    },
    loadData(item, callback) {
      loadDepartment(item.id, { openDataFilter: false }).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            e.selected = false;
            if (e.isParent) {
              e.loading = false;
              e.children = [];
            }
            if (e.status == -1) {
              e.title = "[已禁用] " + e.title;
              e.disabled = true;
            }
          });
          callback(res.result);
        }
      });
    },
    editDep(v) {
      this.dataType = 0;
      this.editRolePermId = v.id;
      this.modalTitle = "分配 " + v.name + " 的数据权限";
      if (v.dataType) {
        this.dataType = v.dataType;
      }
      // 匹配勾选
      let roleDepIds = v.departments;
      this.editDepartments = roleDepIds;
      this.depTreeLoading = true;
      initDepartment({ openDataFilter: false }).then(res => {
        this.depTreeLoading = false;
        if (res.success) {
          res.result.forEach(function(e) {
            e.selected = false;
            if (e.isParent) {
              e.loading = false;
              e.children = [];
            }
            if (e.status == -1) {
              e.title = "[已禁用] " + e.title;
              e.disabled = true;
            }
          });
          this.depData = res.result;
          // 判断子节点
          this.checkDepTree(this.depData, roleDepIds);
        }
      });
      this.depModalVisible = true;
    },
    expandCheckDep(v) {
      // 判断展开子节点
      this.checkDepTree(v.children, this.editDepartments);
    },
    // 判断子节点
    checkDepTree(depData, roleDepIds) {
      let that = this;
      depData.forEach(function(p) {
        if (that.hasDepPerm(p, roleDepIds)) {
          p.selected = true;
        } else {
          p.selected = false;
        }
      });
    },
    // 判断角色拥有的权限节点勾选
    hasDepPerm(p, roleDepIds) {
      let flag = false;
      for (let i = 0; i < roleDepIds.length; i++) {
        if (p.id == roleDepIds[i].departmentId) {
          flag = true;
          break;
        }
      }
      if (flag) {
        return true;
      }
      return false;
    },
    submitDepEdit() {
      let depIds = "";
      if (this.dataType == 1) {
        let selectedNodes = this.$refs.depTree.getSelectedNodes();
        selectedNodes.forEach(function(e) {
          depIds += e.id + ",";
        });
        depIds = depIds.substring(0, depIds.length - 1);
      }
      this.submitDepLoading = true;
      editRoleDep({
        roleId: this.editRolePermId,
        dataType: this.dataType,
        depIds: depIds
      }).then(res => {
        this.submitDepLoading = false;
        if (res.success) {
          this.$Message.success("操作成功");
          this.getRoleList();
          this.depModalVisible = false;
        }
      });
    },
    changeOpen(v) {
      if (v == "0") {
        this.permData.forEach(e => {
          e.expand = true;
          if (e.children && e.children.length > 0) {
            e.children.forEach(c => {
              c.expand = true;
              if (c.children && c.children.length > 0) {
                c.children.forEach(function(b) {
                  b.expand = true;
                });
              }
            });
          }
        });
      } else if (v == "1") {
        this.permData.forEach(e => {
          e.expand = false;
          if (e.children && e.children.length > 0) {
            e.children.forEach(c => {
              c.expand = false;
              if (c.children && c.children.length > 0) {
                c.children.forEach(function(b) {
                  b.expand = false;
                });
              }
            });
          }
        });
      } else if (v == "2") {
        this.permData.forEach(e => {
          e.expand = true;
          if (e.children && e.children.length > 0) {
            e.children.forEach(c => {
              c.expand = false;
              if (c.children && c.children.length > 0) {
                c.children.forEach(function(b) {
                  b.expand = false;
                });
              }
            });
          }
        });
      } else if (v == "3") {
        this.permData.forEach(e => {
          e.expand = true;
          if (e.children && e.children.length > 0) {
            e.children.forEach(c => {
              c.expand = true;
              if (c.children && c.children.length > 0) {
                c.children.forEach(function(b) {
                  b.expand = false;
                });
              }
            });
          }
        });
      }
    }
  },
  mounted() {
    this.init();
    this.selectArrayFlag = false;
    this.selectDictTimeInitFlag = false;
  },
  created(){
  },
  destroyed(){
  },
};
</script>