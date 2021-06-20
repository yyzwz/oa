<style lang="less">
@import "../../../styles/table-common.less";
@import "./archive.less";
</style>
<template>
  <div class="search">
    <Card>
      <Row v-show="openSearch" @keydown.enter.native="handleSearch">
        <Form ref="searchForm" :model="searchForm" inline :label-width="70">
          <Form-item label="部门名" prop="username">
            <Input
              type="text"
              v-model="searchForm.title"
              clearable
              placeholder="请输入部门名称"
              style="width: 200px"
            />
          </Form-item>
          <Form-item label="职责" prop="username">
            <Input
              type="text"
              v-model="searchForm.duty"
              clearable
              placeholder="请输入部门职责"
              style="width: 200px"
            />
          </Form-item>
          <Form-item style="margin-left:-35px;" class="br">
            <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
            <Button @click="handleReset" type="warning" icon="md-refresh">重置</Button>
          </Form-item>
        </Form>
      </Row>
      <Row class="operation">
        <Button @click="add" type="primary" icon="md-add">添加部门</Button>
        <Button @click="delAll" type="error" icon="md-trash">批量删除</Button>
        <Dropdown @on-click="handleDropdown">
          <Button>
            更多操作
            <Icon type="md-arrow-dropdown" />
          </Button>
          <DropdownMenu slot="list">
            <DropdownItem name="refresh">刷新</DropdownItem>
            <DropdownItem name="exportData">导出所选数据</DropdownItem>
            <DropdownItem name="exportAll">导出全部数据</DropdownItem>
            <DropdownItem name="importData">导入数据</DropdownItem>
          </DropdownMenu>
        </Dropdown>
        <Button type="dashed" @click="openSearch=!openSearch">{{openSearch ? "关闭搜索" : "开启搜索"}}</Button>
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
          sortable="custom"
          @on-sort-change="changeSort"
          @on-selection-change="showSelect"
          ref="table"
        ></Table>
      </Row>
      <Row type="flex" justify="end" class="page">
        <Page
          :current="searchForm.pageNumber"
          :total="total"
          :page-size="searchForm.pageSize"
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

    <!-- 自定义导出数据 -->
    
      <addEdit :data="form" :type="showType" v-model="showDepartmentArchive" @on-submit="getData" />
      <!-- 自定义导出数据 -->
    <Modal
      v-model="exportModalVisible"
      :title="exportTitle"
      :loading="loadingExport"
      @on-ok="exportCustomData"
    >
      <Form ref="exportForm" :label-width="100">
        <FormItem label="导出文件名">
          <Input v-model="filename" />
        </FormItem>
        <FormItem label="自定义导出列">
          <CheckboxGroup v-model="chooseColumns">
            <Checkbox
              v-for="(item, i) in exportColumns"
              :label="item.title"
              :key="i"
              :value="item.checked"
              :disabled="item.disabled"
            ></Checkbox>
          </CheckboxGroup>
        </FormItem>
      </Form>
    </Modal>

    <Drawer title="导入数据" closable v-model="importModalVisible" width="1000">
      <div style="display:flex;justify-content: space-between;align-items: center;">
        <Upload action :before-upload="beforeUploadImport" accept=".xls, .xlsx">
          <Button
            :loading="reading"
            icon="ios-cloud-upload-outline"
            style="margin-right:10px"
          >上传Excel文件</Button>
          <span v-if="uploadfile.name">当前选择文件：{{ uploadfile.name }}</span>
        </Upload>
        <Button @click="clearImportData" icon="md-trash">清空数据</Button>
      </div>
      <Alert type="warning" show-icon>导入前请下载查看导入模版数据文件，确保数据格式正确，不得修改列英文名称。必需数据字段：用户名(唯一)、密码(明文)</Alert>
      <Table
        :columns="importColumns"
        border
        :height="height"
        :data="importTableData"
        ref="importTable"
      ></Table>
      <div class="drawer-footer">
        <Button @click="downloadTemple" type="info">下载导入模板</Button>
        <div style="position:absolute;right:15px;display: inline-block">
          <Button @click="importModalVisible=false">关闭</Button>
          <Button
            :loading="importLoading"
            :disabled="importTableData.length<=0"
            @click="importData"
            style="margin-left:8px"
            type="primary"
          >
            确认导入
            <span v-if="importTableData.length>0">{{importTableData.length}} 条数据</span>
          </Button>
        </div>
      </div>
    </Drawer>
    
  </div>
</template>

<script>
import {
  getArchiveListData,
  deleteDepartmentArchive,
  getAllDepartmentArchiveData,
  importDepartmentArchiveData
} from "./index";
import { exportColumn } from "./exportColumn";
import { departmentArchiveColumns, departmentArchiveData } from "@/libs/importTemplate";
import excel from "@/libs/excel";
import addEdit from "./add.vue";
export default {
  name: "departmentArchive-manage",
  components: {
    addEdit
  },
  data() {
    return {
      height: 510,
      showDepartmentArchive: false,
      showType: "0",
      openSearch: true,
      openTip: true,
      loading: true,
      reading: false,
      importLoading: false,
      loadingExport: true,
      exportModalVisible: false,
      importModalVisible: false,
      drop: false,
      dropDownContent: "展开",
      dropDownIcon: "ios-arrow-down",
      selectCount: 0,
      selectList: [],
      searchForm: {
        id: "",
        departmentArchivename: "",
        pageNumber:1,
        pageSize:10,
      },
      selectDate: null,
      form: {},
      exportColumns: exportColumn,
      chooseColumns: [],
      filename: "用户数据",
      exportTitle: "确认导出",
      exportType: "",
      importTableData: [],
      importColumns: [],
      uploadfile: {
        name: ""
      },
      tempColumns: departmentArchiveColumns,
      tempData: departmentArchiveData,
      data: [],
      exportData: [],
      columns: [
        {
          type: "selection",
          minWidth: 60,
          align: "center",
          fixed: "left"
        },
        {
          title: '序号',
          minWidth: 60,
          align: "center",
          fixed: "left",
          sortType:true,
          sortable: true,
          render: (h,params) => {
            return h('span',params.index + (this.searchForm.pageNumber-1)*this.searchForm.pageSize + 1 )
          }
        },
        {
          title: "部门名称",
          key: "title",
          minWidth: 150,
          sortable: true,
        },
        {
          title: "部门代码",
          key: "code",
          minWidth: 150,
          sortable: true,
        },
        {
          title: "部门职责",
          key: "duty",
          minWidth: 200,
          sortable: true,
        },
        {
          title: "创建时间",
          key: "createTime",
          sortable: true,
          minWidth: 200,
          sortType: "desc"
        },
        {
          title: "操作",
          key: "action",
          minWidth: 200,
          align: "center",
          fixed: "right",
          render: (h, params) => {
            let enableOrDisable = "";
            
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
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
              enableOrDisable,
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
      chooseColumns: [],
      filename: "部门数据",
      exportTitle: "确认导出",
      exportType: "",
      importTableData: [],
      importColumns: [],
      uploadfile: {
        name: ""
      },
      data: [],
      exportData: [],
      total: 0,
      dictSex: this.$store.state.dict.sex
    };
  },
  methods: {
    init() {
      this.getData();
      let array = [];
      this.exportColumns.forEach(e => {
        // 指定列限制权限
        if (
          !this.getStore("roles").includes("ROLE_ADMIN") &&
          e.key == "mobile"
        ) {
          e.title = "[无权导出] " + e.title;
          e.disabled = true;
        } else {
          e.disabled = false;
        }
        array.push(e.title);
      });
      this.chooseColumns = array;
    },
    exportCustomData() {
      if (this.filename == "") {
        this.filename = "用户数据";
      }
      // 判断勾选导出列
      let array = [];
      this.exportColumns.forEach(e => {
        this.chooseColumns.forEach(c => {
          if (e.title == c && !e.disabled) {
            array.push(e);
          }
        });
      });
      this.exportColumns = array;
      this.exportModalVisible = false;
      let title = [];
      let key = [];
      this.exportColumns.forEach(e => {
        title.push(e.title);
        key.push(e.key);
      });
      const params = {
        title: title,
        key: key,
        data: this.exportData,
        autoWidth: true,
        filename: this.filename
      };
      excel.export_array_to_excel(params);
    },
    getData() {
      this.loading = true;
      getArchiveListData(this.searchForm).then(res => {
        this.loading = false;
        if (res.success) {
          this.data = res.result.records;
          this.total = res.result.total;
        }
      });
    },
    beforeUploadImport(file) {
      this.uploadfile = file;
      const fileExt = file.name
        .split(".")
        .pop()
        .toLocaleLowerCase();
      if (fileExt == "xlsx" || fileExt == "xls") {
        this.readFile(file);
        this.file = file;
      } else {
        this.$Notice.warning({
          title: "文件类型错误",
          desc:
            "所选文件‘ " +
            file.name +
            " ’不是EXCEL文件，请选择后缀为.xlsx或者.xls的EXCEL文件。"
        });
      }
      return false;
    },
    // 读取文件
    readFile(file) {
      this.reading = true;
      const reader = new FileReader();
      reader.readAsArrayBuffer(file);
      reader.onerror = e => {
        this.reading = false;
        this.$Message.error("文件读取出错");
      };
      reader.onload = e => {
        const data = e.target.result;
        const { header, results } = excel.read(data, "array");
        const tableTitle = header.map(item => {
          return { title: item, key: item, minWidth: 100, align: "center" };
        });
        this.importTableData = results;
        this.importColumns = tableTitle;
        this.reading = false;
        this.$Message.success("读取数据成功");
      };
    },
    clearImportData() {
      this.importTableData = [];
      this.importColumns = [];
      this.uploadfile = {};
    },
    downloadTemple() {
      let title = [];
      let key = [];
      departmentArchiveColumns.forEach(e => {
        title.push(e.title);
        key.push(e.key);
      });
      const params = {
        title: title,
        key: key,
        data: departmentArchiveData,
        autoWidth: true,
        filename: "导入部门数据模版"
      };
      excel.export_array_to_excel(params);
    },
    importData() {
      this.importLoading = true;
      importDepartmentArchiveData(this.importTableData).then(res => {
        this.importLoading = false;
        if (res.success) {
          this.importModalVisible = false;
          this.getData();
          this.$Modal.info({
            title: "导入结果",
            content: res.message
          });
        }
      });
    },
    changePage(v) {
      this.searchForm.pageNumber = v;
      this.getData();
      this.clearSelectAll();
    },
    changePageSize(v) {
      this.searchForm.pageSize = v;
      this.getData();
    },
    selectDateRange(v) {
      if (v) {
        this.searchForm.startDate = v[0];
        this.searchForm.endDate = v[1];
      }
    },
    handleSearch() {
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.getData();
    },
    handleReset() {
      this.$refs.searchForm.resetFields();
      this.searchForm.pageNumber = 1;
      this.searchForm.pageSize = 10;
      this.searchForm.title = "";
      this.getData();
    },
    changeSort(e) {
      this.searchForm.sort = e.key;
      this.searchForm.order = e.order;
      if (e.order == "normal") {
        this.searchForm.order = "";
      }
      this.getData();
    },
    handleDropdown(name) {
      if (name == "refresh") {
        this.getData();
      }else if (name == "exportData") {
        if (this.selectCount <= 0) {
          this.$Message.warning("您还未选择要导出的数据");
          return;
        }
        this.exportType = "part";
        this.exportModalVisible = true;
        this.exportTitle = "确认导出 " + this.selectCount + " 条数据";
      } else if (name == "exportAll") {
        this.exportType = "all";
        this.exportModalVisible = true;
        this.exportTitle = "确认导出全部 " + this.total + " 条数据";
        getAllDepartmentArchiveData().then(res => {
          if (res.success) {
            this.exportData = res.result;
          }
        });
      } else if (name == "importData") {
        this.importModalVisible = true;
      }
    },
    showDetail(v) {
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.form = data;
      this.showType = "0";
      this.showDepartmentArchive = true;
    },
    add() {
      this.showType = "2";
      this.showDepartmentArchive = true;
    },
    edit(v) {
      for (let attr in v) {
        if (v[attr] == null) {
          v[attr] = "";
        }
      }
      let str = JSON.stringify(v);
      let data = JSON.parse(str);
      this.form = data;
      this.showType = "1";
      this.showDepartmentArchive = true;
    },
    remove(v) {
      this.$Modal.confirm({
        title: "确认删除",
        content: "您确认要删除部门 " + v.title + " ?",
        loading: true,
        onOk: () => {
          deleteDepartmentArchive({ids: v.id}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.getData();
            }
          });
        }
      });
    },
    showSelect(e) {
      this.exportData = e;
      this.selectList = e;
      this.selectCount = e.length;
    },
    clearSelectAll() {
      this.$refs.table.selectAll(false);
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
          deleteDepartmentArchive({ids: ids}).then(res => {
            this.$Modal.remove();
            if (res.success) {
              this.$Message.success("删除成功");
              this.clearSelectAll();
              this.getData();
            }
          });
        }
      });
    }
  },
  mounted() {
    this.height = Number(document.documentElement.clientHeight - 230);//test
    this.init();
  }
};
</script>