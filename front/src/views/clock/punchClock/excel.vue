<style lang="less">
</style>
<template>
  <div>
    <Button type="success" @click="importShow" icon="md-trash" shape="circle">导入考勤打卡数据</Button>
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
  importDingtalkClock,
} from "@/api/index";
import { validateMobile } from "@/libs/validate";
// 模版导入文件表数据
import { userColumns, userData } from "@/libs/importTemplate";
// 指定导出列数据
import { exportColumn } from "./exportColumn";
import excel from "@/libs/excel";
export default {
  name: "excelclock",
  components: {
  },
  data() {
    return {
      height:'510',
      importModalVisible: false,
      reading: false,
      uploadfile: {
        name: ""
      },
      importColumns: [],
      importTableData: [],
    };
  },
  methods: {
    init() {
      
    },
    importShow(){
      this.importModalVisible = true;
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
    clearImportData() {
      this.importTableData = [];
      this.importColumns = [];
      this.uploadfile = {};
    },
    downloadTemple() {
      let title = [];
      let key = [];
      userColumns.forEach(e => {
        title.push(e.title);
        key.push(e.key);
      });
      const params = {
        title: title,
        key: key,
        data: userData,
        autoWidth: true,
        filename: "导入用户数据模版"
      };
      excel.export_array_to_excel(params);
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
    importData() {
      this.importLoading = true;
      importDingtalkClock(this.importTableData).then(res => {
        this.importLoading = false;
        if (res.success) {
          this.importModalVisible = false;
          this.$Modal.info({
            title: "导入结果",
            content: res.message
          });
        }
      });
    },
  },
  mounted() {
    this.height = Number(document.documentElement.clientHeight - 230);
    this.init();
  }
};
</script>