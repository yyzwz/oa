<template>
  <div>
    123456
    <Drawer title="导入花名册数据" closable v-model="isImportHuaData" width="1000">
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
          <Button @click="isImportHuaData=false">退出</Button>
        </Row>
      </car>
    </Drawer>
  </div>
</template>

<script>
export default {
    data() {
        return {
        // 是否展示花名册导入抽屉
        isImportHuaData: true,
        }
    },
    props: ['isImportHuaData'],
    huaAddData() {
      if (this.selectHuaCount <= 0) {
        this.$Message.warning("您还未选择要从花名册导入的数据");
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
          //console.log(ids);
          importHuaByIds({ids:ids}).then(res => {
            this.$Modal.remove();
            //console.log(res);
            if(res.success){
              this.$Message.success("导入成功");
              this.clearSelectAll();
              this.getUserList();
              this.getHuaUserList();
              this.isImportHuaData=false;
            }
          })
          // deleteUser({ids: ids}).then(res => {
          //   this.$Modal.remove();
          //   if (res.success) {
          //     this.$Message.success("导入成功");
          //     this.clearSelectAll();
          //     this.getUserList();
          //   }
          // });
        }
      });
    },
    getHuaUserList() {
      // 多条件搜索用户列表
      // this.loading = true;
      getHuaListData().then(res => {
        // this.loading = false;
        if (res.success) {
          //console.log(res);
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
}
</script>

<style>

</style>