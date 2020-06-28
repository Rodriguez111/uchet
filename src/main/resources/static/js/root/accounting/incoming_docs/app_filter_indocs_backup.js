    var filter_comp = Vue.component('app-filter-indocs', {
        template: `
     <tr class="main_table_tr">
              <td class="filter_cell_td">
              <input class="input_filter"  v-model="params.docNumber" :class="{'input-is-not-empty-style': params.docNumber !== ''}" @keyup.enter="sendData()"></td> 
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params._date_docDate" :class="{'input-is-not-empty-style': params._date_docDate !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.contractor$name" :class="{'input-is-not-empty-style': params.contractor$name !== ''}" @keyup.enter="sendData()"></td>                                
                
                 <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.type$docType" :class="{'input-is-not-empty-style': params.type$docType !== ''}" @keyup.enter="sendData()"></td> 
                
                <td class="filter_cell_td">
                <input class="input_filter"   v-model="params.owner$surnameANDowner$name" :class="{'input-is-not-empty-style': params.owner$surnameANDowner$name !== ''}" @keyup.enter="sendData()"></td>                
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                
            </tr>
    
    `,
        data() {
            return {
                params: {
                    docNumber: '',
                    _date_docDate: '',
                    contractor$name: '',
                    type$docType:'',
                    owner$surnameANDowner$name: '',
                    _count_details:'',
                    columnToSort: '',
                    directionToSort: ''
                }
            }
        },
        methods: {
            sendData() {
                inDocsApp.listOfRows = [];
                indocsControllerGetFiltered.save(this.params).then(result => result.json().then(data => data.forEach(row => {
                    inDocsApp.listOfRows.push(row);
                    }
                )))
            },

        },
    });
