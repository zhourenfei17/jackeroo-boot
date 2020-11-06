<template>
  <table class="jackeroo-draggable-table">
    <thead>
      <tr>
        <th>#</th>
        <th v-for="col in columns" :key="col.dataIndex">{{col.title}}</th>
      </tr>
    </thead>
    <draggable v-model="rows" tag="tbody" v-bind="dragOptions" handle=".drag-icon">
      <tr v-for="(data, index) in rows" :key="index">
        <td><a-icon v-if="!data.disabled" type="menu" class="drag-icon"></a-icon></td>
        <td v-for="(col, index2) in columns" :key="index + '_' + index2">{{col.customRender ? col.customRender(data[col.dataIndex], data, index) : data[col.dataIndex]}}</td>
      </tr>
    </draggable>
  </table>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: 'dragTable',
  components: {
    draggable
  },
  props: {
    columns: {
      type: Array,
      required: true
    },
    dataSource: {
      type: Array
    },
    rowKey: {
      type: String,
      default: "id"
    }
  },
  created(){
    this.rows = [...this.dataSource]
  },
  data() {
    return {
      rows: []
    }
  },
  computed: {
    dragOptions() {
      return {
        animation: 500,
      }
    }
  },
  methods: {
    getData(){
      return this.rows
    }
  }
}
</script>

<style lang="less" scoped>
  .jackeroo-draggable-table{
    width: 100%;

    thead{
      font-size: 14px;
      font-weight: bolder;

      tr{
        th{
          border-bottom: 1px solid #e8e8e8;
          transition: background 0.3s ease;
          background-color: #fafafa;
          padding: 8px;
        }
      }
    }

    tbody{
      tr{
        td{
          border-bottom: 1px solid #e8e8e8;
          padding: 8px;
        }
      }
    }
  }

  .flip-list-move{
    transition: transform 0.5s;
  }
  .on-move{
    transition: transform 0s;
  }
  .ghost {
    opacity: 0.5;
    background: #c8ebfb;
  }
  .drag-icon{
    cursor: pointer;
    font-size: 20px;
  }
</style>