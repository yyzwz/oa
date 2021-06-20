<style lang="less">
@import "../../../styles/tree-common.less";
@import "../../../styles/common.less";
</style>
<template>
  <div class="search">
    <Card>
      <Row class="operation">
        <Button @click="add" type="primary" icon="md-add" v-show="showType=='tree'">添加子角色组</Button>
        <Button @click="addRoot" icon="md-add">添加一级角色组</Button>
        <Button @click="delAll" icon="md-trash">批量删除</Button>
        <Button @click="getParentList" icon="md-refresh">刷新</Button>
        <Input
          v-model="searchKey"
          suffix="ios-search"
          @on-change="search"
          placeholder="输入角色组名搜索"
          clearable
          style="width: 250px"
          v-show="showType=='list'"
        />
        <i-switch v-model="strict" size="large" v-show="showType=='tree'" style="margin-left:5px">
          <span slot="open">级联</span>
          <span slot="close">单选</span>
        </i-switch>
        <div style="float: right">
          <RadioGroup v-model="showType" type="button">
            <Radio title="树结构" label="tree">
              <Icon type="md-list"></Icon>
            </Radio>
            <Radio title="列表" label="list">
              <Icon type="ios-apps"></Icon>
            </Radio>
          </RadioGroup>
        </div>
      </Row>
      <Row type="flex" justify="start" v-show="showType=='tree'">
        <Col :md="8" :lg="8" :xl="6" style="width:20%">
          <Alert show-icon>
            当前选择编辑角色组：
            <p><span class="select-title">{{editTitle}}</span></p>
            <a class="select-clear" v-if="form.id" @click="cancelEdit">取消选择</a>
          </Alert>
          <Input
            v-model="searchKey"
            suffix="ios-search"
            @on-change="search"
            placeholder="输入角色组名搜索"
            clearable
          />
          <div class="tree-bar" :style="{maxHeight: maxHeight}">
            <Tree
              ref="tree"
              :data="data"
              :load-data="loadData"
              show-checkbox
              @on-check-change="changeSelect"
              @on-select-change="selectTree"
              :check-strictly="true"
            ></Tree>
            <Spin size="large" fix v-if="loading"></Spin>
          </div>
        </Col>
        <Col :md="15" :lg="13" :xl="9" style="margin-left:10px;width:79%">
           <Collapse   accordion >
              <Panel  class=" ivu-alert-info ivu-alert-with-icon">
                角色组详细
                <div slot="content" >
                  <Form ref="form" :model="form" :label-width="100" :rules="formValidate">
                    <FormItem label="上级角色组" prop="parentTitle">
                      <div style="display:flex;">
                        <Input v-model="form.parentTitle" readonly style="margin-right:10px; width:300px" />
                        <Poptip transfer trigger="click" placement="right-start" title="选择上级角色组" width="250">
                          <Button icon="md-list">选择角色组</Button>
                          <div slot="content" style="position:relative;min-height:5vh">
                            <Tree :data="dataEdit" :load-data="loadData" @on-select-change="selectTreeEdit"></Tree>
                            <Spin size="large" fix v-if="loadingEdit"></Spin>
                          </div>
                        </Poptip>
                      </div>
                    </FormItem>
                    <FormItem label="角色组名称" prop="title">
                      <Input v-model="form.title" style="width:300px" />
                    </FormItem>
                    <FormItem label="备注" prop="description">
                      <Input v-model="form.description" style="width:400px" type="textarea"/>
                    </FormItem>
                    <FormItem label="排序值" prop="sortOrder">
                      <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                        <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                      </Tooltip>
                    </FormItem>
                    <FormItem label="是否启用" prop="status">
                      <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                        <span slot="open">启用</span>
                        <span slot="close">禁用</span>
                      </i-switch>
                    </FormItem>
                    <Form-item class="br">
                      <Button
                        @click="submitEdit"
                        :loading="submitLoading"
                        type="primary"
                        icon="ios-create-outline"
                      >修改并保存</Button>
                      <Button @click="handleReset">重置</Button>
                    </Form-item>
                  </Form>
                </div>
              </Panel>
            </Collapse>
            

          <Divider class="divider"/>
            <div v-show="showType=='tree'">
              <!-- {{propForm}} -->
            <roleManage v-bind:roleGroup="propForm"></roleManage>
          </div>
        </Col>
      </Row>
      <Alert show-icon v-show="showType=='list'">
        已选择
        <span class="select-count">{{selectCount}}</span> 项
        <a class="select-clear" @click="clearSelectAll">清空</a>
      </Alert>
      <Table
        row-key="title"
        :load-data="loadData"
        :columns="columns"
        :data="data"
        :loading="loading"
        border
        ref="table"
        @on-selection-change="showSelect"
        @on-row-click="handleRow"
        v-if="showType=='list'"
      ></Table>
    </Card>
    <div v-show="showType=='list'">
          <roleManage v-bind:roleGroup="propForm"></roleManage>
    </div>
    <!-- <huaImport v-bind:roleGroup="propForm"></huaImport> -->
     <!-- 编辑 -->
    <Modal :title="modalTitle" v-model="roleModalVisible" :mask-closable="false" :width="500">
      <Form ref="roleForm" :model="roleForm" :label-width="80" :rules="roleFormValidate">
        <FormItem label="角色名称" prop="name">
          <Input v-model="roleForm.name" placeholder="按照Spring Security约定建议以‘ROLE_’开头" />
        </FormItem>
        <FormItem label="备注" prop="description">
          <Input v-model="roleForm.description" />
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelRole">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitRole">提交</Button>
      </div>
    </Modal>
    <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500">
      <Form width="400px" style="width:400px"  ref="formAdd" :model="formAdd" :label-width="100" :rules="formValidate">
        <div v-if="showParent">
          <FormItem label="上级角色组：">{{form.title}}</FormItem>
        </div>
        <FormItem label="角色组名称" prop="title">
          <Input v-model="formAdd.title" />
        </FormItem>
        <FormItem label="备注" prop="description">
          <Input v-model="form.description" type="textarea"/>
        </FormItem>
        <FormItem label="排序值" prop="sortOrder">
          <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
            <InputNumber :max="1000" :min="0" v-model="formAdd.sortOrder"></InputNumber>
          </Tooltip>
        </FormItem>
        <FormItem label="是否启用" prop="status">
          <i-switch size="large" v-model="formAdd.status" :true-value="0" :false-value="-1">
            <span slot="open">启用</span>
            <span slot="close">禁用</span>
          </i-switch>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelAdd">取消</Button>
        <Button type="primary" :loading="submitLoading" @click="submitAdd">提交</Button>
      </div>
    </Modal>
    
  </div>
</template>

<script>
import roleManage from "./roleManage";
import huaImport from "./import.vue"
import {
  getAllRoleGroupInTree,
  initRoleGroup,
  loadRoleGroup,
  addRoleGroup,
  editRoleGroup,
  deleteRoleGroup,
  searchRoleGroup
} from "./index";
export default {
  name: "roleGroup-manage",
  components: {
    roleManage,
    huaImport
  },
  data() {
    return {
      modalTitle: "",
      roleForm: {
        name: "",
        description: ""
      },
      roleModalVisible: false,
      showType: "tree",
      loading: true,
      maxHeight: "500px",
      strict: true,
      userLoading: false,
      loadingEdit: true,
      modalVisible: false,
      selectList: [],
      selectCount: 0,
      showParent: false,
      modalTitle: "",
      editTitle: "",
      searchKey: "",
      form: {
        id: "",
        title: "",
        parentId: "",
        parentTitle: "",
        sortOrder: 0,
        status: 0
      },
      propForm:{},
      formAdd: {},
      formValidate: {
        title: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        sortOrder: [
          {
            required: true,
            type: "number",
            message: "排序值不能为空",
            trigger: "blur"
          }
        ]
      },
      submitLoading: false,
      data: [],
      dataEdit: [],
      users: [],
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
          title: "角色组名称",
          key: "title",
          minWidth: 120,
          sortable: true,
          tree: true
        },
        {
          title: "排序",
          key: "sortOrder",
          width: 150,
          sortable: true,
          align: "center",
          sortType: "asc"
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          width: 200
        },
        {
          title: "操作",
          key: "action",
          width: 300,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small",
                    icon: "md-add"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.tableAdd(params.row);
                    }
                  }
                },
                " 添加子角色组"
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
      ]
    };
  },
  methods: {
    init() {
      this.getParentList();
      this.getParentListEdit();

    },
    getParentList() {
      this.loading = true;
      getAllRoleGroupInTree().then(res => {
        this.loading = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              //e.children = [];
              e._loading = false;
            }
          });
          this.data = res.result;
        }
      });
    },
    getParentListEdit() {
      this.loadingEdit = true;
      initRoleGroup().then(res => {
        this.loadingEdit = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children = [];
            }
          });
          // 头部加入一级
          let first = {
            id: "0",
            title: "一级角色组"
          };
          res.result.unshift(first);
          this.dataEdit = res.result;
        }
      });
    },
    loadData(item, callback) {
      loadRoleGroup(item.id).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children = [];
              e._loading = false;
            }
          });
          callback(res.result);
        }
      });
    },
    search() {
      if (this.searchKey) {
        this.loading = true;
        searchRoleGroup({ title: this.searchKey }).then(res => {
          this.loading = false;
          if (res.success) {
            this.data = res.result;
          }
        });
      } else {
        this.getParentList();
      }
    },
    selectTree(v) {
      if (v.length > 0) {
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] == null) {
            v[0][attr] = "";
          }
        }
        let str = JSON.stringify(v[0]);
        let data = JSON.parse(str);
        this.editTitle = data.title;
        // 回显
        this.form = data;
        this.propForm=data;
      } else {
        this.cancelEdit();
      }
    },
    cancelEdit() {
      let data = this.$refs.tree.getSelectedNodes()[0];
      if (data) {
        data.selected = false;
      }
      this.$refs.form.resetFields();
      delete this.form.id;
      this.propForm={id:-1,title:''};
      this.editTitle = "";
    },
    selectTreeEdit(v) {
      if (v.length > 0) {
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] == null) {
            v[0][attr] = "";
          }
        }
        let str = JSON.stringify(v[0]);
        let data = JSON.parse(str);
        this.form.parentId = data.id;
        this.form.parentTitle = data.title;
      }
    },
    cancelAdd() {
      this.modalVisible = false;
    },
    handleReset() {
      this.$refs.form.resetFields();
      this.form.status = 0;
    },
    handleRow(row, index) {
      this.selectIndex=index;
      this.editTitle=row.title;
      this.propForm=row;
    },
    showSelect(e) {
      this.selectList = e;
      this.selectCount = e.length;
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
      
    },
    submitEdit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (!this.form.id) {
            this.$Message.warning("请先点击选择要修改的角色组");
            return;
          }
          this.submitLoading = true;
          editRoleGroup(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("编辑成功");
              this.init();
              this.modalVisible = false;
            }
          });
        }
      });
    },
    submitAdd() {
      this.$refs.formAdd.validate(valid => {
        if (valid) {
          this.submitLoading = true;
          addRoleGroup(this.formAdd).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.$Message.success("添加成功");
              this.init();
              this.modalVisible = false;
            }
          });
        }
      });
    },
    tableAdd(v) {
      this.form = v;
      this.add();
    },
    add() {
      if (this.form.id == "" || this.form.id == null) {
        this.$Message.warning("请先点击选择一个角色组");
        return;
      }
      this.modalTitle = "添加子角色组";
      this.showParent = true;
      this.formAdd = {
        parentId: this.form.id,
        sortOrder: 0,
        status: 0
      };
      this.modalVisible = true;
    },
    addRoot() {
      this.modalTitle = "添加一级角色组";
      this.showParent = false;
      this.formAdd = {
        parentId: 0,
        sortOrder: 0,
        status: 0
      };
      this.modalVisible = true;
    },
    changeSelect(v) {
      this.selectCount = v.length;
      this.selectList = v;
    },
    remove(v) {
      this.selectCount = 1;
      this.selectList.push(v);
      this.delAll();
    },
    delAll() {
      if (this.selectCount <= 0) {
        this.$Message.warning("您还未勾选要删除的数据");
        return;
      }
      this.$Modal.confirm({
        title: "确认删除",
        content:
          "您确认要删除所选的 " + this.selectCount + " 条数据及其下级所有数据?",
        loading: true,
        onOk: () => {
          let ids = "";
          this.selectList.forEach(function(e) {
            ids += e.id + ",";
          });
          ids = ids.substring(0, ids.length - 1);
          deleteRoleGroup({ids: ids}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.selectList = [];
              this.selectCount = 0;
              this.cancelEdit();
              this.init();
            }
          });
        }
      });
    }
  },
  mounted() {
    // 计算高度
    let height = document.documentElement.clientHeight;
    this.maxHeight = Number(height - 287) + "px";
    this.init();
  }
};
</script>