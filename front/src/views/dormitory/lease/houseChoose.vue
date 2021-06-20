<template>
  <div class="user-choose">
    <Button @click="userModalVisible=true" :icon="icon">{{text}}</Button>
    
    <Drawer title="选择宿舍" closable v-model="userModalVisible" width="800" draggable>
      <Form ref="searchUserForm" :model="searchUserForm" inline :label-width="55">
        <Form-item label="门牌号" prop="houseNumber">
          <Input
            type="text"
            v-model="searchUserForm.houseNumber"
            clearable
            placeholder="请输入宿舍门牌号"
            style="width: 200px"
          />
        </Form-item>
        <Form-item style="margin-left:-35px;" class="br">
          <Button @click="handleSearchUser" type="primary" icon="ios-search">搜索</Button>
          <Button @click="handleResetUser">重置</Button>
        </Form-item>
      </Form>
      <Table
        :loading="userLoading"
        border
        :columns="userColumns"
        :data="userData"
        @on-row-click="changeSelect"
        style="margin: 2vh 0;"
      ></Table>
      <Row type="flex" justify="end">
        <Page
          :current="searchUserForm.pageNumber"
          :total="totalUser"
          :page-size="searchUserForm.pageSize"
          @on-change="changeUserPage"
          @on-page-size-change="changeUserPageSize"
          :page-size-opts="[10,20,50]"
          size="small"
          show-total
          show-elevator
          show-sizer
          transfer
        ></Page>
      </Row>
    </Drawer>
  </div>
</template>

<script>
import { getDormitoryListData } from "@/api/index";
export default {
  name: "userChoose",
  components: {
  },
  props: {
    text: {
      type: String,
      default: "选择宿舍"
    },
    icon: {
      type: String,
      default: "md-person-add"
    }
  },
  data() {
    return {
      userLoading: true,
      userModalVisible: false,
      selectUsers: [],
      searchUserForm: {
        id: "",
        nickname: "",
        type: "",
        status: "",
        pageNumber: 1, // 当前页数
        pageSize: 10, // 页面大小
        sort: "createTime", // 默认排序字段
        order: "desc" // 默认排序方式
      },
      userColumns: [
        // {
        //   type: "selection",
        //   width: 60,
        //   align: "center"
        // },
        {
          title: "宿舍门牌号",
          key: "houseNumber",
          minWidth: 130
        },
        {
          title: "房型",
          key: "houseType",
          minWidth: 130
        },
        {
          title: "面积",
          key: "areaMeasure",
          minWidth: 120,
          sortable: true
        },
        {
          title: "租金",
          key: "rent",
          minWidth: 120,
          sortable: true
        }
      ],
      userData: [],
      totalUser: 0
    };
  },
  methods: {
    changeSelect(e){
      this.$emit("on-change", e);
      this.userModalVisible = false;
    },
    changeUserPage(v) {
      this.searchUserForm.pageNumber = v;
      this.getDormitoryDataList();
    },
    changeUserPageSize(v) {
      this.searchUserForm.pageSize = v;
      this.getDormitoryDataList();
    },
    getDormitoryDataList() {
      this.userLoading = true;
      getDormitoryListData(this.searchUserForm).then(res => {
        this.userLoading = false;
        if (res.success) {
          this.userData = res.result.records;
          this.totalUser = res.result.total;
        }
      });
    },
    handleSearchUser() {
      this.searchUserForm.pageNumber = 1;
      this.searchUserForm.pageSize = 10;
      this.getDormitoryDataList();
    },
    handleResetUser() {
      this.$refs.searchUserForm.resetFields();
      this.searchUserForm.pageNumber = 1;
      this.searchUserForm.pageSize = 10;
      this.$refs.dep.clearSelect();
      this.searchUserForm.departmentId = "";
      // 重新加载数据
      this.getDormitoryDataList();
    },
    clearSelectData() {
      this.selectUsers = [];
      this.$emit("on-change", this.selectUsers);
    },
    handleCancelUser(e, id) {
      // 删除所选用户
      this.selectUsers = this.selectUsers.filter(e => {
        return e.id != id;
      });
      this.$emit("on-change", this.selectUsers);
      this.$Message.success("删除所选用户成功");
    }
  },
  mounted() {
    this.getDormitoryDataList();
  }
};
</script>

<style lang="less">
.user-choose {
  .clear {
    font-size: 12px;
    margin-left: 15px;
    color: #40a9ff;
    cursor: pointer;
  }
  .collapse {
    font-size: 12px;
    margin-top: 15px;
    width: 500px;
  }
  .select-count {
    font-weight: 600;
    color: #40a9ff;
  }
}
</style>