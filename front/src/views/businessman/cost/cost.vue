<style lang="less">
@import "../../../styles/table-common.less";
@import "./cost.less";
</style>
<template>
  <Tabs :value="chooseName" @on-click="changeTab">

    <TabPane label="房租月租金" name="name1">
      <main1></main1>
    </TabPane>

    <TabPane label="房租年租金" name="name2">
      <main2></main2>
    </TabPane>

    <TabPane label="人员工资月费用" name="name3">
      <main3></main3>
    </TabPane>

    <TabPane label="人员工资年费用" name="name4">
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