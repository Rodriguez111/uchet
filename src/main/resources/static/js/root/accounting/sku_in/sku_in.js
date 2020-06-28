let indocDetailsControllerGetAll = Vue.resource(contextPath + '/sku_in_controller/all');
let indocDetailsControllerFiltered = Vue.resource(contextPath + '/sku_in_controller/local_filter');



Vue.component('app-skuins', {
    props: [
        'listOfRows',
        'tableUpdateToggle'
    ],
    template: `<tbody>
        <tr class="main_table_tr" v-for="(row, index) in listOfRows">
        <td class="main_table_td column_1">{{index + 1}}</td>
        <td class="main_table_td column_2">{{row.inDoc.docNumber}}</td>
        <td class="main_table_td column_3">{{row.sku.code}}</td>
        <td class="main_table_td column_4">{{row.sku.name}} </td>
        <td class="main_table_td column_5">{{row.serial}} </td>
        <td class="main_table_td column_6">{{row.expireDate}} </td>
        <td class="main_table_td column_7">{{row.qty}}</td>   
        <td class="main_table_td column_8">{{row.sku.unit.unit}}</td>  
        <td class="main_table_td column_9">{{row.price}}</td>  
        <td class="main_table_td column_10">{{row.vatPrice}}</td>  
        <td class="main_table_td column_11">{{row.total}}</td>  
        <td class="main_table_td column_12">{{row.vatTotal}}</td>  
        <td class="main_table_td column_13">{{row.inDoc.docDate}}</td>  
        <td class="main_table_td column_14">{{row.inDoc.contractor.name}}</td>  
        <td class="main_table_td column_15">{{row.inDoc.type.docType}}</td>  
        <td class="main_table_td column_16">{{row.inDoc.owner.fullName}}</td>         
        </tr>        
        </tbody>`,
    created: function () {
        indocDetailsControllerGetAll.get().then(result => result.json().then(data => data.forEach(row => {
                this.listOfRows.push(row)
            }
        )))
        console.log(this.listOfRows)
    },
    watch: {
        tableUpdateToggle: function () {
            inDocsApp.listOfRows = [];
            indocsControllerGetAll.get().then(result => result.json().then(data => data.forEach(row => {
                    this.listOfRows.push(row)
                }
            )))
        }
    },

});












var skuinApp = new Vue({
    el: '#skuin-app',
    data() {
        return {
            listOfRows: [],

            toggleForUpdate: true,
        }
    }

});

