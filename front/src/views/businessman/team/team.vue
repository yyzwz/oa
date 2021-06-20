<style lang="less">
@import "../../../styles/table-common.less";
@import "./team.less";
</style>
<template>
  <Tabs :value="chooseName" @on-click="changeTab">

    <TabPane label="负责人档案" name="name1">
      <main1></main1>
    </TabPane>

    <TabPane label="组织架构档案" name="name2">
      <main2></main2>
    </TabPane>

    <TabPane label="人员档案" name="name3">
      <main3></main3>
    </TabPane>
  </Tabs>
</template>

<script>

import main1 from "./main1.vue";
import main2 from "./main2.vue";
import main3 from "./main3.vue";
export default {
  name: "biaoshi-manage",
  components: {
    main1,
    main2,
    main3
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