Vue.component('app-filter-positions', {
        template: `
     <tr class="main_table_tr">
              <td class="filter_cell_td">
              <input class="input_filter"  v-model="params.position" :class="{'input-is-not-empty-style': params.position !== ''}" @keyup.enter="sendData()"></td> 
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params.positionDescription" :class="{'input-is-not-empty-style': params.positionDescription !== ''}" @keyup.enter="sendData()"></td>               
                <td class="filter_cell_td"></td>
                <td class="filter_cell_td"></td>
            </tr>
    
    `,
        data() {
            return {
                params: {
                    position: '',
                    positionDescription: '',
                    columnToSort: '',
                    directionToSort: ''
                }
            }
        },
        methods: {
            sendData() {
                positionsApp.listOfPositions = [];
                positionsControllerGetFiltered.save(this.params).then(result => result.json().then(data => data.forEach(position => {
                    positionsApp.listOfPositions.push(position);

                    }
                )))
            },
        },
    });
