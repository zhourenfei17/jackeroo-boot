<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible.sync="visible"
      :disabled="flag.view"
      okText="更改排序"
      @ok="handleSubmit"
      @close="cancel"
    >
    <j-spin :spinning="loading">
      
      <!-- <table>
        <tr>
          <td>原始排序号</td>
          <td>列名</td>
          <td>列描述</td>
        </tr>
        <draggable v-model="dataSource">
          <tr v-for="row in dataSource" :key="row.dbFieldName">
            <td>{{row.sort}}</td>
            <td>{{row.dbFieldName}}</td>
            <td>{{row.dbFieldDesc}}</td>
          </tr>
        </draggable>
      </table> -->
      <drag-table :columns="columns" :dataSource="dataSource" rowKey="dbFieldName" ref="dragTable">

      </drag-table>
    </j-spin>
  </j-drawer>
</template>

<script>
import {JDrawer, JSpin, DragTable} from '@/components'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  components: {
    JDrawer,
    JSpin,
    DragTable
  },
  mixins: [JackerooFromMixins],
  data() {
    return {
      title: '数据列排序',
      width: '40vw',
      columns: [
        {
          title: '当前顺序',
          customRender: (v, r, i) => {
            return i + 1
          }
        },
        {
          dataIndex: 'dbFieldName',
          title: '列名'
        },
        {
          dataIndex: 'dbFieldDesc',
          title: '列描述'
        },
        {
          dataIndex: 'sort',
          title: '原始顺序'
        },
      ],
      dataSource: [],
      components: {
        body: {
          row: ({index, moveRow, className, style, ...restProps}) => {
            return (
              <tr className={className} style={style} {...restProps} />
            )
          }
        }
      }
    }
  },
  methods: {
    edit(dataSource){
      this.dataSource = dataSource
      this.visible = true
      this.loading = false
    },
    handleSubmit(){
      let data = this.$refs.dragTable.getData()
      this.$emit('ok', data)
      this.cancel()
    },
    cancel(){
      this.visible = false
      this.loading = true
    }
  }
}
</script>