<style lang="less">
@import "../../../styles/table-common.less";
@import "./mianJiDangAn.less";
</style>
<template>
  <Tabs :value="chooseName" @on-click="changeTab">

    <TabPane label="商场位置" name="name1">
      <main1></main1>
    </TabPane>

    <TabPane label="市场位置" name="name2">
      <main2></main2>
    </TabPane>

  </Tabs>
</template>

<script>

import main1 from "./main1.vue";
import main2 from "./main2.vue";
export default {
  name: "shichangweizhi-manage",
  components: {
    main1,
    main2,
  },
  data() {
    return {
      chooseName:'name1'
    };
  },
  methods: {
    changeTab(value){
      //console.log(value);
      this.chooseName = value;
    }
  }
}
</script>