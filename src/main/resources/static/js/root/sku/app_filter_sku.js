    var filter_comp = Vue.component('app-filter-sku', {
        template: `
     <tr class="main_table_tr">
              <td class="filter_cell_td">
              <input class="input_filter"  v-model="params.code" :class="{'input-is-not-empty-style': params.code !== ''}" @keyup.enter="sendData()"></td> 
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params.name" :class="{'input-is-not-empty-style': params.name !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params.contractor$name" :class="{'input-is-not-empty-style': params.contractor$name !== ''}" @keyup.enter="sendData()"></td>                                
                <td class="filter_cell_td">
                <input class="input_filter"   v-model="params.unit$unit" :class="{'input-is-not-empty-style': params.unit$unit !== ''}" @keyup.enter="sendData()"></td>                
                <td class="filter_cell_td">
                <input class="input_filter" v-model="params._numeric_quantityInSecondaryPackaging" :class="{'input-is-not-empty-style': params._numeric_quantityInSecondaryPackaging !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td">
                <input class="input_filter"  v-model="params._boolean_isActive" :class="{'input-is-not-empty-style': params._boolean_isActive !== ''}" @keyup.enter="sendData()"></td>
                <td class="filter_cell_td"></td>
                <td class="filter_cell_td"></td>
            </tr>
    
    `,
        data() {
            return {
                params: {
                    code: '',
                    name: '',
                    contractor$name: '',
                    unit$unit: '',
                    _numeric_quantityInSecondaryPackaging: '',
                    _boolean_isActive: '',
                    columnToSort: '',
                    directionToSort: ''
                }
            }
        },
        methods: {
            sendData() {
                skuApp.listOfSku = [];
                skuControllerGetFiltered.save(this.params).then(result => result.json().then(data => data.forEach(sku => {
                        skuApp.listOfSku.push(sku);
                    }
                )))
            },
        },
    });
