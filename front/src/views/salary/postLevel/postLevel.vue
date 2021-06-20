<style lang="less">
@import "../../../styles/tree-common.less";
@import "../../../styles/common.less";
</style>
<template>
  <div class="search">
    <Card>
      <Row class="operation">
        <Button @click="add" type="success" icon="md-add">添加子岗位</Button>
        <Button @click="addRoot" type="warning" icon="md-add">添加一级岗位</Button>
        <Button @click="delAll" type="error" icon="md-trash">批量删除</Button>
        <Button @click="getParentList" type="info" icon="md-refresh">刷新页面</Button>
        <i-switch v-model="strict" size="large" style="margin-left:5px">
          <span slot="open">单选</span>
          <span slot="close">级联</span>
        </i-switch>
      </Row>
      <Row type="flex" justify="start">
        <Col :md="8" :lg="8" :xl="6">
          <Alert show-icon>
            当前选择编辑：
            <span class="select-title">{{editTitle}}</span>
            <a class="select-clear" v-if="form.id" @click="cancelEdit">取消选择</a>
          </Alert>
          <Input
            v-model="searchKey"
            suffix="ios-search"
            @on-change="search"
            placeholder="输入岗位名搜索"
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
              :check-strictly="strict"
            ></Tree>
            <Spin size="large" fix v-if="loading"></Spin>
          </div>
        </Col>
        <Col :md="24" :lg="17" :xl="100" style="margin-left:30px;">
          <Collapse accordion :value="collapseFlag">
            <Panel name="1" class=" ivu-alert-info ivu-alert-with-icon">
              岗位详细
              <div slot="content" >
                <Form ref="form" :model="form" :label-width="100" :rules="formValidate">
                  <Row>
                    <Col>
                      <FormItem label="上级岗位" prop="parentTitle">
                        <div style="display:flex;">
                          <Input v-model="form.parentTitle" readonly style="margin-right:10px;" />
                          <Poptip transfer trigger="click" placement="right-start" title="选择上级岗位" width="250">
                            <Button icon="md-list">选择岗位</Button>
                            <div slot="content" style="position:relative;min-height:5vh">
                              <Tree :data="dataEdit" :load-data="loadData" @on-select-change="selectTreeEdit"></Tree>
                              <Spin size="large" fix v-if="loadingEdit"></Spin>
                            </div>
                          </Poptip>
                        </div>
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col>
                      <FormItem label="岗位名称" prop="title">
                        <Input v-model="form.title" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="11">
                      <FormItem label="基本工资" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.baseSalary" />
                      </FormItem>
                    </Col>
                    <Col span="11">
                      <FormItem label="全勤奖" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.fullReward" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="11">
                      <FormItem label="餐费补贴" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.malAllowance" />
                      </FormItem>
                    </Col>
                    <Col span="11">
                      <FormItem label="交通补贴" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.transportationSubsidy" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="11">
                      <FormItem label="通信补贴" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.communicationSubsidy" />
                      </FormItem>
                    </Col>
                    <Col span="11">
                      <FormItem label="生日奖金" v-if="!this.form.isParent">
                        <Input v-model="welfareFrom.birthdayBonus" />
                      </FormItem>
                    </Col>
                  </Row>
                  <Row>
                    <Col span="11">
                      <FormItem label="是否启用" prop="status">
                        <i-switch size="large" v-model="form.status" :true-value="0" :false-value="-1">
                          <span slot="open">启用</span>
                          <span slot="close">禁用</span>
                        </i-switch>
                      </FormItem>
                    </Col>
                    <Col span="11">
                      <FormItem label="排序值" prop="sortOrder">
                        <Tooltip trigger="hover" placement="right" content="值越小越靠前，支持小数">
                          <InputNumber :max="1000" :min="0" v-model="form.sortOrder"></InputNumber>
                        </Tooltip>
                      </FormItem>
                    </Col>
                  </Row>
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
          <userPostLevel :selectPostId="selectPostId" :selectPostName="selectPostName"/>
        </Col>
      </Row>
    </Card>

    <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500">
      <Form ref="formAdd" :model="formAdd" :label-width="85" :rules="formValidate">
        <div v-if="showParent">
          <FormItem label="上级岗位：">{{form.title}}</FormItem>
        </div>
        <FormItem label="岗位名称" prop="title">
          <Input v-model="formAdd.title" />
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
import {
  getOneWelfare,
  editWelfare,
  getAllJueSeMangerInTree,
  initJueSeManger,
  loadJueSeManger,
  addJueSeManger,
  editJueSeManger,
  deleteJueSeManger,
  searchJueSeManger,
  getUserByProfessionId
} from "./index";
import userPostLevel from './userPostLevel.vue';
export default {
  name: "profession-manage",
  components: {
    userPostLevel
  },
  data() {
    return {
      collapseFlag:'-1',
      selectPostId:'-1',
      selectPostName:'',
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
      welfareFrom:{},
      form: {
        id: "",
        title: "",
        parentId: "",
        parentTitle: "",
        sortOrder: 0,
        status: 0,
        remark:"",
        requirement:""
      },
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
          title: "岗位名称",
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
                " 添加子岗位"
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
      getAllJueSeMangerInTree().then(res => {
        this.loading = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              //e.children1 = [];
              e._loading = false;
            }
          });
          this.data = res.result;
        }
      });
    },
    getParentListEdit() {
      this.loadingEdit = true;
      initJueSeManger().then(res => {
        this.loadingEdit = false;
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children1 = [];
              e.expand = false;//保证选择岗位时，树形岗位不展开
            }
          });
          // 头部加入一级
          let first = {
            id: "0",
            title: "一级岗位"
          };
          res.result.unshift(first);
          this.dataEdit = res.result;
        }
      });
    },
    loadData(item, callback) {
      loadJueSeManger(item.id).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children1 = [];
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
        searchJueSeManger({ title: this.searchKey }).then(res => {
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
      // this.collapseFlag = "1"; // 自动展开
      this.selectPostId = v[0].id;
      this.selectPostName = v[0].title;
      // console.log(this.selectPostId);
      // 请求某个岗位的福利数据
      getOneWelfare({id:v[0].welfare}).then(res =>{
        // console.log(res.result);
        this.welfareFrom = res.result;
      })
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
        // 加载岗位用户数据
        this.userLoading = true;
        // 回显
        this.form = data;
         
      
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
      this.editTitle = "";
      this.selectPostId = "-1";
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
            this.$Message.warning("请先点击选择要修改的岗位");
            return;
          }
          Vue.delete(this.form,'children');
          // Vue.delete(this.form,'updateTime');
          this.submitLoading = true;
          editJueSeManger(this.form).then(res => {
            this.submitLoading = false;
            if (res.success) {
              this.welfareFrom.updateTime = this.form.updateTime;
              this.welfareFrom.updateBy = this.form.updateBy;
              editWelfare(this.welfareFrom).then(res =>{
                // console.log(res);
              });
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
          addJueSeManger(this.formAdd).then(res => {
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
        this.$Message.warning("请先点击选择一个岗位");
        return;
      }
      this.modalTitle = "添加子岗位";
      this.showParent = true;
      this.formAdd = {
        parentId: this.form.id,
        sortOrder: 0,
        status: 0
      };
      this.modalVisible = true;
    },
    addRoot() {
      this.modalTitle = "添加一级岗位";
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
          deleteJueSeManger({ids: ids}).then(res => {
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