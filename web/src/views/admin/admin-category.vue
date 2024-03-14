<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form-item>
          <a-button type="primary" @click="add()">
            新增
          </a-button>
        </a-form-item>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="categories"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:bodyCell="{ column, record }">
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
          :model="categoryEdit"
          name="basic"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="名称">
          <a-input v-model:value="categoryEdit.name" />
        </a-form-item>
        <a-form-item label="父分类">
          <a-input v-model:value="categoryEdit.parent" />
        </a-form-item>
        <a-form-item label="顺序">
          <a-input v-model:value="categoryEdit.sort" />
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
    const categories = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
      },
      {
        title: '父分类',
        dataIndex: 'parent'
      },
      {
        title: '顺序',
        dataIndex: 'sort'
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
        }
      };
      axios.get("/category/list", config).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categories.value = data.content.list;
          pagination.value.current = queryParams.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }

      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize,
      });
    };

    // 编辑按钮弹窗功能
    const open = ref<boolean>(false);
    const modalConfirmLoading = ref<boolean>(false);

    // 编辑弹窗的组件
    const categoryEdit = ref();

    const edit = (record: any) => {
      open.value = true;
      categoryEdit.value = Tool.copy(record);
    };

    // 新增
    const add = () => {
      open.value = true;
      categoryEdit.value = {};
    }

    const handleDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
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
      axios.post("/category/save", categoryEdit.value).then((response) => {
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
      categories,
      pagination,
      columns,
      loading,
      open,
      modalConfirmLoading,
      categoryEdit,
      edit,
      add,
      modalHandleOk,
      handleDelete,
      handleTableChange,
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
