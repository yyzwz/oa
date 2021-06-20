<style lang="less">
@import "../../../styles/table-common.less";
@import "./storeLogo.less";
</style>
<template>
  <Tabs :value="chooseName" @on-click="changeTab">

    <TabPane label="店铺物料位置" name="name1">
      <main1></main1>
    </TabPane>

    <TabPane label="通信位置" name="name2">
      <main2></main2>
    </TabPane>

    <TabPane label="商号名称" name="name3">
      <main3></main3>
    </TabPane>

    <TabPane label="店面形象管理" name="name4">
      <main4></main4>
    </TabPane>

  </Tabs>
</template>

<script>

import main1 from "./main1.vue";
import main2 from "./main2.vue";
import main3 from "./main3.vue";
import main4 from "./main4.vue";
export default {
  name: "biaoshi-manage",
  components: {
    main1,
    main2,
    main3,
    main4
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