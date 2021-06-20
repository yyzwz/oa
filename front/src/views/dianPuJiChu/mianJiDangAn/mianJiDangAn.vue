<style lang="less">
@import "../../../styles/table-common.less";
@import "./mianJiDangAn.less";
</style>
<template>
  <Tabs :value="chooseName" @on-click="changeTab">

    <TabPane label="总面积" name="name1">
      <main1></main1>
    </TabPane>

    <TabPane label="单品类面积" name="name2">
      <main2></main2>
    </TabPane>

  </Tabs>
</template>

<script>

import main1 from "./main1.vue";
import main2 from "./main2.vue";
export default {
  name: "mianji-manage",
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