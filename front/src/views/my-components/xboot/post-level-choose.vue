<template>
  <div>
    <div style="display:flex;">
      <Input
        v-model="postLevelTitle"
        readonly
        style="margin-right:10px;"
        :placeholder="placeholder"
        :clearable="clearable"
        @on-clear="clearSelect"
      />
      <Poptip transfer trigger="click" placement="right" title="选择岗位" width="250">
        <Button icon="md-list">选择岗位</Button>
        <div slot="content">
          <Input
            v-model="searchKey"
            suffix="ios-search"
            @on-change="searchDep"
            placeholder="输入岗位名搜索"
            clearable
          />
          <div class="dep-tree-bar">
            <Tree
              :data="dataDep"
              :load-data="loadData"
              @on-select-change="selectTree"
              :multiple="multiple"
            ></Tree>
            <Spin size="large" fix v-if="depLoading"></Spin>
          </div>
        </div>
      </Poptip>
    </div>
  </div>
</template>

<script>
import { initPostLevel, loadPostLevel, searchPostLevel } from "@/api/index";
export default {
  name: "PostLevelChoose",
  props: {
    multiple: {
      type: Boolean,
      default: false
    },
    clearable: {
      type: Boolean,
      default: true
    },
    placeholder: {
      type: String,
      default: "点击选择岗位"
    }
  },
  data() {
    return {
      depLoading: false,
      postLevelTitle: "",
      searchKey: "",
      dataDep: [],
      selectDep: [],
      postLevelId: []
    };
  },
  methods: {
    initPostLevelData() {
      initPostLevel().then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
            if (e.isParent) {
              e.loading = false;
              e.children = [];
            }
            if (e.status == -1) {
              e.title = "[已禁用] " + e.title;
              e.disabled = true;
            }
          });
          this.dataDep = res.result;
        }
      });
    },
    loadData(item, callback) {
      loadPostLevel(item.id).then(res => {
        if (res.success) {
          res.result.forEach(function(e) {
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
    searchDep() {
      // 搜索岗位
      if (this.searchKey) {
        this.depLoading = true;
        searchPostLevel({ title: this.searchKey }).then(res => {
          this.depLoading = false;
          if (res.success) {
            res.result.forEach(function(e) {
              if (e.status == -1) {
                e.title = "[已禁用] " + e.title;
                e.disabled = true;
              }
            });
            this.dataDep = res.result;
          }
        });
      } else {
        this.initPostLevelData();
      }
    },
    selectTree(v) {
      let ids = [],
        title = "";
      v.forEach(e => {
        ids.push(e.id);
        if (title == "") {
          title = e.title;
        } else {
          title = title + "、" + e.title;
        }
      });
      this.postLevelId = ids;
      this.postLevelTitle = title;
      if (this.multiple) {
        this.$emit("on-change", this.postLevelId);
      } else {
        this.$emit("on-change", this.postLevelId[0]);
      }
    },
    clearSelect() {
      this.postLevelId = [];
      this.postLevelTitle = "";
      this.initPostLevelData();
      if (this.multiple) {
        this.$emit("on-change", []);
      } else {
        this.$emit("on-change", "");
      }
      this.$emit("on-clear");
    },
    setData(ids, title) {
      this.postLevelTitle = title;
      if (this.multiple) {
        this.postLevelId = ids;
      } else {
        this.postLevelId = [];
        this.postLevelId.push(ids);
      }
      this.$emit("on-change", this.postLevelId);
    }
  },
  mounted() {
    this.initPostLevelData();
  }
};
</script>

<style lang="less">
.dep-tree-bar {
  position: relative;
  min-height: 80px;
  max-height: 500px;
  overflow: auto;
  margin-top: 5px;
}

.dep-tree-bar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.dep-tree-bar::-webkit-scrollbar-thumb {
  border-radius: 4px;
  -webkit-box-shadow: inset 0 0 2px #d1d1d1;
  background: #e4e4e4;
}
</style>

