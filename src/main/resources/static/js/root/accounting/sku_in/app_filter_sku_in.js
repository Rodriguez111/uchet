var filter_comp = Vue.component('app-filter-skuin', {
    template: `
     <tr class="main_table_tr">     
              <td class="filter_cell_td"></td> 
                   
              <td class="filter_cell_td">
              <input class="input_filter"  v-model="params.inDoc$docNumber" :class="{'input-is-not-empty-style': params.inDoc$docNumber !== ''}" @keyup.enter="sendData()"></td> 
              
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params.sku$code" :class="{'input-is-not-empty-style': params.sku$code !== ''}" @keyup.enter="sendData()"></td>
                
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.sku$name" :class="{'input-is-not-empty-style': params.sku$name !== ''}" @keyup.enter="sendData()"></td>                                
                
                 <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.serial" :class="{'input-is-not-empty-style': params.serial !== ''}" @keyup.enter="sendData()"></td> 
                
                <td class="filter_cell_td">
                <input class="input_filter"   v-model="params.owner$surnameANDowner$name" :class="{'input-is-not-empty-style': params.owner$surnameANDowner$name !== ''}" @keyup.enter="sendData()"></td>                
                
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                 <td class="filter_cell_td">
                <input class="input_filter" v-model="params._count_details" :class="{'input-is-not-empty-style': params._count_details !== ''}" @keyup.enter="sendData()"></td>
                
                
                
                
                
            </tr>
    
    `,
    data() {
        return {
            params: {
                inDoc$docNumber: '',
                sku$code:'',
                sku$name:'',
                serial:'',


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
            skuinApp.listOfRows = [];
            indocDetailsControllerFiltered.save(this.params).then(result => result.json().then(data => data.forEach(row => {
                skuinApp.listOfRows.push(row);
                }
            )))
        },

    },
});
