<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'cover'">
            <img v-if="record.cover" :src="record.cover" alt="avatar" />
          </template>
          <template v-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit">
                编辑
              </a-button>
              <a-button danger>
                删除
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
    <a-modal v-model:open="open" title="删除" :confirm-loading="modalConfirmLoading" @ok="modalHandleOk">
      <p>{{ modalText }}</p>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';

export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类一',
        dataIndex: 'category1Id'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action'
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (queryParams: any) => {
      loading.value = true;
      const config = {
        params: {
          page: queryParams.page,
          size: queryParams.size
        }
      };
      axios.get("/ebook/list", config).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

        pagination.value.current = queryParams.page;
        pagination.value.total = data.content.total;
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    // 编辑按钮弹窗功能
    const modalText = ref<string>('Content of the modal');
    const open = ref<boolean>(false);
    const modalConfirmLoading = ref<boolean>(false);

    const edit = () => {
      open.value = true;
    };

    const modalHandleOk = () => {
      modalText.value = 'The modal will be closed after two seconds';
      modalConfirmLoading.value = true;
      setTimeout(() => {
        open.value = false;
        modalConfirmLoading.value = false;
      }, 2000);
    };

    return {
      ebooks,
      pagination,
      columns,
      loading,
      modalText,
      open,
      modalConfirmLoading,
      edit,
      modalHandleOk,
      handleTableChange
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
