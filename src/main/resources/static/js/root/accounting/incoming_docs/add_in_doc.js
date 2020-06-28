/*

*
*
* */


let contractorsControllerApi = Vue.resource(contextPath + '/contractors_controller/suppliers');
let skuControllerBySupId = Vue.resource(contextPath + '/sku_controller/get_by_contractor_id/{id}');
let inDocController = Vue.resource(contextPath + '/incoming_docs_controller');
let inDocTypeController = Vue.resource(contextPath + '/incoming_doc_type_controller/all');




Vue.component('contractors-list', {
    template: `
     <div>
<select v-model="selected" @change="changeContractorSource()"  class="form_input" size="5">
      <option v-for="contractor in listOfContractors" v-bind:value="{ id: contractor.id, name: contractor.name }" >{{contractor.name}} [{{contractor.contractorCode}}]</option>
        </select>
       
        <warning-popup header_title="Смена поставщика"
                   button_1_name="OK"
                   button_2_name="Отмена"
                   v-if="isWarningPopupVisible"                   
                   @warning_result="warningResult">
                   <span class="popup_title">Все внесенные данные не сохранятся.<br>Продолжить?</span>
    </warning-popup>
        
</div>
    `,
    data() {
        return {
            listOfContractors: [],
            selected: {},
            contractorName: "",
            previousSelected: {}, /*для выделения поставщика в случае отмены*/
            isWarningPopupVisible: false,
        }
    },
    methods: {
        changeContractorSource() {
            if (addInDocApp.detailsList.length > 0) {
                this.showWarning();
            } else {
                this.previousSelected = this.selected;
                this.getSkuListForContractor();
                addInDocApp.currentContractorName = this.selected.name;
            }
        },
        getSkuListForContractor() {
            addInDocApp.detailsList = [];
            addInDocApp.skuList = [];
            skuControllerBySupId.get({id: this.selected.id}).then(result => result.json().then(data => data.forEach(sku => {
                    addInDocApp.skuList.push(sku)
                }
            )))
        },
        warningResult(result) {
            if (result) {
                this.previousSelected = this.selected;
                this.getSkuListForContractor();
            } else {
                this.selected = this.previousSelected;
            }
            this.isWarningPopupVisible = false;
        },
        showWarning() {
            this.isWarningPopupVisible = true
        },
    },
    created() {
        contractorsControllerApi.get().then(result => result.json().then(data => data.forEach(contractor => {
                this.listOfContractors.push(contractor)
            }
        )))
    }
});

var skuListComp = Vue.component('sku-list', {
    props: {
        listOfSku: {
            type: Array
        }
    }
    ,
    template: `
     <div>
            <select v-model="selected" size="7" style="height: 140px" class="form_input" multiple >
              <option v-for="sku in listOfSku" v-bind:value="sku" @dblclick="addDetail(sku)">[{{sku.code}}] {{sku.name}}</option>                   
            </select>
     </div>
    `,
    data() {
        return {
            selected:[]
        }
    },
    methods: {
        addMultiplyDetails() {
            for(var i = 0; i < this.selected.length; i++) {
                this.addDetail(this.selected[i]);
            }
        },
        addDetail(sku) {
            addInDocApp.infoMessage = "";
            var newDetail = this.initiateDetail(sku);
            if (!addInDocApp.detailsListContains(newDetail)) {
                addInDocApp.detailsList.push(newDetail);
            } else {
                addInDocApp.infoMessage = "Позиция с такими параметрами уже есть в списке";
            }
        },
        initiateDetail(sku) {
            var newDetail = _.cloneDeep(sku);
            newDetail.showSerial = false;
            newDetail.showExpire = false;
            newDetail.showQty = false;
            newDetail.showPrice = false;
            newDetail.serial = "";
            newDetail.expireDate = "";
            newDetail.qty = 0.00;
            newDetail.price = 0.00;
            newDetail.vatPrice = 0.00;
            return newDetail;
        }
    },
});



Vue.component('types-list', {
    props: {
        listOfTypes: {
            type: Array
        }
    }
    ,
    template: `
     <div>
            <select v-model="selected" @change="changeType" class="form_input" >
              <option v-for="type in listOfTypes" v-bind:value="type.docType" >{{type.docType}}</option>                   
            </select>
     </div>
    `,
    data() {
        return {
            selected:'Приход от поставщика'
        }
    },
    created() {
        inDocTypeController.get().then(result => result.json().then(data => data.forEach(type => {
                this.listOfTypes.push(type)
            }
        )));
    },
    methods: {
        changeType() {
            addInDocApp.currentType = this.selected;
        }
    }
});


Vue.component('details-list', {
    props: {
        listOfDetails: {
            type: Array
        }
    }
    ,
    template: `
     <div>
        <tr class="details_table_tr" v-for="(detail, index) in listOfDetails" :key='index'>
        <td class="details_table_td column_1">{{index + 1}}</td>
        <td class="details_table_td column_2">{{detail.code}}</td>
        <td class="details_table_td column_3">{{detail.name}}</td>
        
        <td class="details_table_td column_4 editableField" 
        @dblclick="showInputSerial(detail)">
        <serial-input v-if="detail.showSerial" 
        @apply_input_serial="applyInputSerial" @cancel_input_serial="cancelInputSerial" 
        :placeholder="currentDetail.serial"></serial-input>
        <span v-if="!detail.showSerial">{{detail.serial}}</span> </td>
          
        <td class="details_table_td column_5 editableField date_td" @dblclick="showInputExpire(detail)">
        <expire-input v-if="detail.showExpire" 
        @apply_input_expire="applyInputExpire" @cancel_input_expire="cancelInputExpire" 
        :placeholder="currentDetail.expireDate"></expire-input>
        <span v-if="!detail.showExpire">{{detail.expireDate}}</span> </td>
        
        <td class="details_table_td column_6 editableField" @dblclick="showInputQty(detail)">
        <qty-input v-if="detail.showQty" 
        @apply_input_qty="applyInputQty" @cancel_input_qty="cancelInputQty" 
        :placeholder="currentDetail.qty"></qty-input>
         <span v-if="!detail.showQty">{{detail.qty}}</span></td>
         
        <td class="details_table_td column_7">{{detail.unit.unit}}</td>
        
        <td class="details_table_td column_8 editableField" @dblclick="showInputPrice(detail)">
        <price-input v-if="detail.showPrice" 
        @apply_input_price="applyInputPrice" @cancel_input_price="cancelInputPrice" 
        :placeholder="currentDetail.price"></price-input>
         <span v-if="!detail.showPrice">{{detail.price}}</span></td>
        
        <td class="details_table_td column_9">{{(detail.price * detail.vatValue).toFixed(2)}}</td>
        
        <td class="details_table_td column_10">{{(detail.qty * detail.price).toFixed(2)}}</td>
        
        <td class="details_table_td column_11">{{(detail.qty * (detail.price * detail.vatValue).toFixed(2)).toFixed(2)}}</td>
        
        
        
        <td class="details_table_td column_12"><button class="remove_button" @click="removeDetail(index)">Удалить</button></td>
        
        </tr>        
        </div>
    `,

    data() {
        return {
            details: {},
            currentDetail: {},
        }
    },
    methods: {
        showInputSerial(detail) {
            detail.showSerial = true;
            this.currentDetail = detail;

        },
        applyInputSerial(serial) {
            var updatedDetail = _.cloneDeep(this.currentDetail);
            updatedDetail.serial = serial;
            if (addInDocApp.detailsListContains(updatedDetail)) {
                addInDocApp.infoMessage = "Позиция с такими параметрами уже есть в списке";
            } else {
                addInDocApp.infoMessage = "";
                this.currentDetail.serial = serial;
                this.currentDetail.showSerial = false;
                this.currentDetail = {};
            }
        },
        cancelInputSerial() {
            addInDocApp.infoMessage = "";
            this.currentDetail.showSerial = false;
            this.currentDetail = {}
        },
        showInputExpire(detail) {
            detail.showExpire = true;
            this.currentDetail = detail;
        },
        applyInputExpire(expire) {
            var updatedDetail = _.cloneDeep(this.currentDetail);
            updatedDetail.expireDate = expire;
            if (addInDocApp.detailsListContains(updatedDetail)) {
                addInDocApp.infoMessage = "Позиция с такими параметрами уже есть в списке";
            } else {
                addInDocApp.infoMessage = "";
                this.currentDetail.expireDate = expire;
                this.currentDetail.showExpire = false;
                this.currentDetail = {}
            }
        },
        cancelInputExpire() {
            addInDocApp.infoMessage = "";
            this.currentDetail.showExpire = false;
            this.currentDetail = {}
        },
        showInputQty(detail) {
            detail.showQty = true;
            this.currentDetail = detail;
        },
        applyInputQty(qty) {
            var updatedDetail = _.cloneDeep(this.currentDetail);
            updatedDetail.qty = qty;
            this.currentDetail.qty = qty;
            this.currentDetail.showQty = false;
            this.currentDetail = {}
        },
        cancelInputQty() {
            this.currentDetail.showQty = false;
            this.currentDetail = {}
        },


        showInputPrice(detail) {
            detail.showPrice = true;
            this.currentDetail = detail;
        },
        applyInputPrice(price) {
            var updatedDetail = _.cloneDeep(this.currentDetail);
            updatedDetail.qty = price;
            this.currentDetail.price = price;
            this.currentDetail.showPrice = false;
            this.currentDetail = {}
        },
        cancelInputPrice() {
            this.currentDetail.showPrice = false;
            this.currentDetail = {}
        },
        removeDetail(index) {
            addInDocApp.detailsList.splice(index, 1);
        },
    },

});

Vue.component('serial-input', {
    props: {
        placeholder: ''
    },
    mixins: [VueClickaway.mixin],
    template: `
     <input class="detail_input" type="text"  v-model="serial" @keyup.enter="applyInput"  @keyup.escape="cancelInput" v-on-clickaway="cancelInput" >
    `,
    data() {
        return {
            serial: "",
        }
    },
    created() {
        this.serial = this.placeholder;
    },
    methods: {
        applyInput() {
            this.$emit("apply_input_serial", this.serial)
        },
        cancelInput() {
            this.$emit("cancel_input_serial", this.serial)
        },
    }
});

Vue.component('expire-input', {
    props: {
        placeholder: ''
    },
    mixins: [VueClickaway.mixin],
    template: `
<div>
     <input class="date_input" type="date" min="0000-01-01" max="2099-12-31" v-model="expire" @keyup.enter="validateAndApplyInput"    @keyup.escape="cancelInput" v-on-clickaway="cancelInput" >
        <warning-popup header_title="Просроченный товар"
                   button_1_name="OK"
                   button_2_name="Отмена"
                   v-if="isWarningPopupVisible"
                   @warning_result="warningResult"
                   >
                   <span class="popup_title">Введенный срок годности меньше текущей даты.<br>Продолжить?</span>
        </warning-popup>    
</div>
`,
    data() {
        return {
            expire: "",
            isWarningPopupVisible: false,

        }
    },
    created() {
        this.expire = this.placeholder;
    },
    methods: {
        applyInput() {
            this.$emit("apply_input_expire", this.expire);
        },
        cancelInput() {
            this.$emit("cancel_input_expire", this.expire)
        },
        warningResult(result) {
            if (result) {
                this.applyInput();
            }
            this.isWarningPopupVisible = false;
        },
        validateAndApplyInput() {
            if (this.expire.length === 10) {
                if (!moment(this.expire, 'YYYY-MM-DD').isValid()) {
                    addInDocApp.infoMessage = "Неверный формат даты";
                } else if (moment(this.expire).isAfter('2099-12-31', 'year')) {
                    addInDocApp.infoMessage = "Слишком большая дата";
                } else {
                    var expireDate = new Date(this.expire.trim());
                    var now = new Date();
                    if (now > expireDate) {
                        this.isWarningPopupVisible = true;
                    } else {
                        this.applyInput();
                    }
                }
            }
        },
    }
});

Vue.component('qty-input', {
    props: {
        placeholder: ''
    },
    mixins: [VueClickaway.mixin],
    template: `
     <input class="detail_input" type="text" @input="validateInput" v-model="qty" @keyup.enter="validateAndApplyInput"  @keyup.escape="cancelInput" v-on-clickaway="cancelInput" placeholder="000000000.00">
    `,
    data() {
        return {
            qty: Number,
        }
    },
    created() {
        this.qty = this.placeholder === 0 ? '' : this.placeholder;
    },
    methods: {
        applyInput() {
            this.$emit("apply_input_qty", this.qty);
        },
        cancelInput() {
            this.$emit("cancel_input_qty", this.qty)
        },
        validateAndApplyInput() {
            addInDocApp.infoMessage = "";
            if ((/^\d{0,9}(\.\d{0,2}){0,1}$/).test(this.qty)) {
                this.qty = parseFloat(this.qty).toFixed(2);
                this.applyInput()
            } else {
                addInDocApp.infoMessage = "Неверный формат количества";
            }
        },
        validateInput() {
            addInDocApp.infoMessage = "";
            if (!(/^\d{0,9}(\.\d{0,2}){0,1}$/).test(this.qty)) {
                this.qty = this.qty.substring(0, this.qty.length - 1);
                addInDocApp.infoMessage = "Неверный формат количества";
            }
        }
    }
});

Vue.component('price-input', {
    props: {
        placeholder: ''
    },
    mixins: [VueClickaway.mixin],
    template: `
     <input class="detail_input" type="text" @input="validateInput" v-model="price" @keyup.enter="validateAndApplyInput"  @keyup.escape="cancelInput" v-on-clickaway="cancelInput" placeholder="000000000.00">
    `,
    data() {
        return {
            price: Number,
        }
    },
    created() {
        this.price = this.placeholder === 0 ? '' : this.placeholder;
    },
    methods: {
        applyInput() {
            this.$emit("apply_input_price", this.price);
        },
        cancelInput() {
            this.$emit("cancel_input_price", this.price)
        },
        validateAndApplyInput() {
            addInDocApp.infoMessage = "";
            if ((/^\d{0,9}(\.\d{0,2}){0,1}$/).test(this.price)) {
                this.price = parseFloat(this.price).toFixed(2);
                this.applyInput()
            } else {
                addInDocApp.infoMessage = "Неверный формат цены";
            }
        },
        validateInput() {
            addInDocApp.infoMessage = "";
            if (!(/^\d{0,9}(\.\d{0,2}){0,1}$/).test(this.price)) {
                this.price = this.price.substring(0, this.price.length - 1);
                addInDocApp.infoMessage = "Неверный формат цены";
            }
        }
    }
});


var addInDocApp = new Vue({
    el: '#add-indoc-app',
    data() {
        return {
            currentContractorName: "",
            currentType: "Приход от поставщика",
            listOfTypes:[],
            skuList: [],
            detailsList: [],
            infoMessage: ""
        }
    },
    methods: {
        addDetails() {
            this.$refs.skuListComponent.addMultiplyDetails()
        },
        detailsListContains(obj) {
            var result = false;

            for (var i = 0; i < this.detailsList.length; i++) {
                if (this.detailsEqual(this.detailsList[i], obj)) {
                    result = true;
                    break;
                }
            }
            return result;
        },
        detailsEqual(obj1, obj2) {
            var result = true;
            if (obj1 == null || obj2 == null) {
                result = false;
            } else if (obj1.code !== obj2.code) {
                result = false;
            } else if (obj1.serial !== obj2.serial) {
                result = false;
            } else if (obj1.expireDate !== obj2.expireDate) {
                result = false;
            } else if (obj1.price !== obj2.price) {
                result = false;
            }
            return result;
        },
        removeAllDetails() {
            this.detailsList = [];
        },
        validateInDoc() {
            var result = true;
            this.infoMessage = "";
            if (this.detailsList.length === 0) {
                this.infoMessage = "Нельзя сохранить пустой документ";
                result = false;
            }
            for (var i = 0; i < this.detailsList.length; i++) {
                if (this.detailsList[i].qty === 0) {
                    this.infoMessage = "В документе присутствуют позиции с нулевыми количествами";
                    result = false;
                    break;
                }
            }
            return result;
        },
        addNew() {
            var validateResult = this.validateInDoc();
            if (validateResult) {
                var newInDoc = {};
                var newDocType = {};
                newDocType.docType = this.currentType;
                var newContractor = {};
                newContractor.name = this.currentContractorName;
                var newOwner = {};
                newOwner.login = authData.profile.username;
                newInDoc.type = newDocType;
                newInDoc.contractor = newContractor;
                newInDoc.owner = newOwner;
                newInDoc.details = this.prepareDetails(this.detailsList);

                inDocController.save(newInDoc).then(result => result.json().then(data => {
                    let addResult = data.addResult;
                    if (addResult === 'OK') {
                        window.location.href = contextPath + '/accounting/incoming_docs';
                    } else {
                        this.infoMessage = addResult;
                    }
                }));
            }

        },
        prepareDetails(details) {
            var newDetails = [];
            for (var i = 0; i < details.length; i++) {
                var newDetail = {};
                var newSku = {};
                newSku.code = details[i].code;
                newDetail.sku = newSku;
                newDetail.serial = details[i].serial;
                newDetail.qty = details[i].qty;
                newDetail.price = details[i].price;
                newDetail.expireDate = details[i].expireDate;
                newDetails.push(newDetail);
            }
        return newDetails;
        }
    },
    computed: {
        detailsAmount() {
            return  this.detailsList.length;
        },
        qtySum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                var qty = parseFloat(this.detailsList[i].qty);
                    result += qty
            }
            return result;
        },
        priceSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                var price = parseFloat(this.detailsList[i].price);
                result += price
            }
            return result;
        },
        vatPriceSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                var price = parseFloat(this.detailsList[i].price);
                result += price * this.detailsList[i].vatValue;
            }
            return result.toFixed(2);
        },
        totalSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                var qty = parseFloat(this.detailsList[i].qty);
                var price = parseFloat(this.detailsList[i].price);
                result += qty * price;
            }
            return result.toFixed(2);
        },
        vatTotalSum() {
            var result = 0;
            for (var i = 0; i < this.detailsList.length; i++) {
                var qty = parseFloat(this.detailsList[i].qty);
                var vatPrice = (parseFloat(this.detailsList[i].price) * parseFloat(this.detailsList[i].vatValue)).toFixed(2);
                result += qty * vatPrice;
            }
            return result.toFixed(2);
        },
    }
});


