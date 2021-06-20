// table-expand.vue

<template>
    <div>
        <Row class="expand-row">
            <Col span="2">
                <span class="expand-key">登录账户: </span>
                <span class="expand-value">{{ row.username }}</span>
            </Col>
            <Col span="2">
                <span class="expand-key">用户名: </span>
                <span class="expand-value">{{ row.nickname }}</span>
            </Col>
            <Col span="4">
                <span class="expand-key">所属部门: </span>
                <span class="expand-value">{{ row.departmentTitle }}</span>
            </Col>
            <Col span="3">
                <span class="expand-key">手机: </span>
                <span class="expand-value">{{ row.mobile }}</span>
            </Col>
            <Col span="3">
                <span class="expand-key">邮箱: </span>
                <span class="expand-value">{{ row.email }}</span>
            </Col>
            <Col span="2">
                <span class="expand-key">性别: </span>
                <span class="expand-value">{{ row.sex }}</span>
            </Col>
        </Row>
        
    </div>
</template>
<script>
    export default {
        props: {
            row: Object
        }
    };
</script>