<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
            layout="inline"
        >
          <a-form-item>
            <a-input v-model:value="search" placeholder="查询名称"/>
          </a-form-item>
          <a-form-item>
            <a-button
                type="primary"
                html-type="submit"
                :disabled="search === ''"
                @click="handleSearch(search)"
            >
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
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
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，是否确认删除？"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handleDelete(record.id)"
              >
                <a-button danger>
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
    <a-modal v-model:open="open" title="增加" :confirm-loading="modalConfirmLoading" @ok="modalHandleOk">
      <a-form
          :model="ebookEdit"
          name="basic"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="封面">
          <a-input v-model:value="ebookEdit.cover" />
        </a-form-item>
        <a-form-item label="名称">
          <a-input v-model:value="ebookEdit.name" />
        </a-form-item>
        <a-form-item label="分类一">
          <a-input v-model:value="ebookEdit.category1Id" />
        </a-form-item>
        <a-form-item label="分类二">
          <a-input v-model:value="ebookEdit.category2Id" />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea v-model:value="ebookEdit.description" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';
import {Tool} from "@/util/tool";

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
          size: queryParams.size,
          name: queryParams.name
        }
      };
      axios.get("/ebook/list", config).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          ebooks.value = data.content.list;
          pagination.value.current = queryParams.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }

      });
    };

    const handleSearch = (name: any) => {
      handleQuery({
        page: 1,
        name: name,
        size: pagination.value.pageSize
      });
    }

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
        name: search.value
      });
    };

    // 编辑按钮弹窗功能
    const open = ref<boolean>(false);
    const modalConfirmLoading = ref<boolean>(false);

    // 编辑弹窗的组件
    const ebookEdit = ref();

    // 查询按钮
    const search = ref("");

    const edit = (record: any) => {
      open.value = true;
      ebookEdit.value = Tool.copy(record);
    };

    // 新增
    const add = () => {
      open.value = true;
      ebookEdit.value = {};
    }

    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          // 重新加载列表
          handleQuery({
            page: 1,
            size: pagination.value.pageSize
          })
        }
      });
    }

    const modalHandleOk = () => {
      modalConfirmLoading.value = true;
      axios.post("/ebook/save", ebookEdit.value).then((response) => {
        const data = response.data;
        modalConfirmLoading.value = false;
        if (data.success) {
          open.value = false;


          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        } else {
          message.error(data.message);
        }
      });
    };


    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      ebooks,
      pagination,
      columns,
      loading,
      open,
      search,
      modalConfirmLoading,
      ebookEdit,
      edit,
      add,
      modalHandleOk,
      handleDelete,
      handleTableChange,
      handleSearch
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
