Vue.component('details-list', {
    props: {
        listOfDetails: {
            type: Array
        }
    }
    ,
    template: `
     <div>
        <tr class="details_table_tr" v-for="(detail, index) in incomingDoc.details" :key='index'>
        
        <td class="details_table_td column_1">{{index + 1}}</td>
        <td class="details_table_td column_2">{{detail.sku.code}}</td>
        <td class="details_table_td column_3">{{detail.sku.name}}</td>        
        <td class="details_table_td column_4">{{detail.serial}}</td>
        <td class="details_table_td column_5">{{detail.expireDate}}</td>
        <td class="details_table_td column_6">{{detail.qty}}</td>        
        <td class="details_table_td column_7">{{detail.sku.unit.unit}}</td>
        <td class="details_table_td column_8">{{detail.price}}</td>        
        <td class="details_table_td column_9">{{detail.vatPrice}}</td>  
        <td class="details_table_td column_10">{{detail.total}}</td>  
        <td class="details_table_td column_11">{{detail.vatTotal}}</td>       
        
        </tr>        
        </div>
    `,

    data() {
        return {
            incomingDoc: inDoc
        }
    },
});


var incomingDocDetailsApp = new Vue({
    el: '#incoming-doc-details-app',
    data() {
        return {
            detailsList:inDoc.details,
            documentDate: inDoc.docDate,
            documentNumber: inDoc.docNumber,
            documentContractorName: inDoc.contractor.name,
            documentOwner: inDoc.owner.fullName,
            documentType: inDoc.type.docType
        }
    },
    computed: {
        detailsAmount() {
            return this.detailsList.length;
        },
        qtySum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                result += this.detailsList[i].qty;
            }
            return result;
        },
        priceSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                result += this.detailsList[i].price
            }
            return result;
        },
        vatPriceSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                result += this.detailsList[i].vatPrice;
            }
            return result;
        },
        totalSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                result += this.detailsList[i].total;
            }
            return result.toFixed(2);
        },
        vatTotalSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                result += this.detailsList[i].vatTotal;
            }
            return result.toFixed(2);
        },
    }
});


