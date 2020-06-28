let skuControllerGetAll = Vue.resource('./sku_controller/all');
let skuControllerGetFiltered = Vue.resource('./sku_controller/local_filter');
let skuController = Vue.resource('./sku_controller{/id}');
let unitsControllerApi = Vue.resource('./units_controller/all');
let contractorsControllerApi = Vue.resource('./contractors_controller/suppliers');

let appSku = Vue.component('app-sku', {
    props: [
        'listOfSku',
        'tableUpdateToggle'
    ],
    template: `<div>
        <tr class="main_table_tr" v-for="sku in listOfSku">
        <td class="main_table_td column_1">{{sku.code}}</td>
        <td class="main_table_td column_2">{{sku.name}}</td>
        <td class="main_table_td column_3">{{sku.contractor.contractorCode}} {{sku.contractor.name}}</td>
        <td class="main_table_td column_4">{{sku.unit.unit}} </td>
        <td class="main_table_td column_5">{{sku.quantityInSecondaryPackaging}}</td>
        <td class="main_table_td column_6">{{sku.active ? "Да" : "Нет"}}</td>
        <td class="main_table_td column_7"><button @click="showUpdatePopup(sku.id)">Изменить</button></td>
        <td class="main_table_td column_8"><button @click="showDeletePopup(sku.id)">Удалить</button></td>
        </tr>        
        </div>`,
    created: function () {
        skuControllerGetAll.get().then(result => result.json().then(data => data.forEach(sku => {
                this.listOfSku.push(sku)
            }
        )))
    },
    watch: {
        tableUpdateToggle: function () {
            skuApp.listOfSku = [];
            skuControllerGetAll.get().then(result => result.json().then(data => data.forEach(sku => {
                    this.listOfSku.push(sku)
                }
            )))
        }
    },
    methods: {
        showUpdatePopup(skuId) {
            skuApp.showUpdatePopup(skuId)
        },
        showDeletePopup(skuId) {
            skuApp.showDeletePopup(skuId)
        }
    }

});


Vue.component('app-unit-for-add', {
    data() {
        return {
            listOfUnits: [],
            units_input: 'шт.'
        }
    },
    template: `
<div>
<select v-model="units_input" @change="assignUnitToNewSku" class="form_input" >
      <option v-for="unit in listOfUnits" v-bind:value="unit.unit">{{unit.unit}}</option>
        </select>
</div>`,
    created: function () {
        skuApp.newObject.unit = this.units_input;
        unitsControllerApi.get().then(result => result.json().then(data => data.forEach(unit => {
                this.listOfUnits.push(unit)
            }
        )))
    },
    methods: {
        assignUnitToNewSku() {
            skuApp.newObject.unit = this.units_input;
        }
    }
});

Vue.component('app-contractor-for-add', {
    props: {
        contractorsErrorEnable: false,
    },
    data() {
        return {
            listOfContractors: [],
            contractors_input: ''
        }
    },
    template: `
<div>
<select v-model="contractors_input" @change="assignContractorToNewSku" :class="{'input-error-style': contractorsErrorEnable}" class="form_input" >
      <option v-for="contractor in listOfContractors" v-bind:value="contractor.name">{{contractor.name}} [{{contractor.contractorCode}}]</option>
        </select>
</div>`,
    created: function () {
        contractorsControllerApi.get().then(result => result.json().then(data => data.forEach(contractor => {
                this.listOfContractors.push(contractor)
            }
        )))
    },
    methods: {
        assignContractorToNewSku() {
            skuApp.newObject.contractor.name = this.contractors_input;
        }
    }
});


Vue.component('app-unit-for-update', {
    props: {
        selectedUnit: ''
    },
    data() {
        return {
            listOfUnits: [],
            units_input: ''
        }
    },
    template: `
<div>
<select v-model="units_input" @change="assignNewUnit" class="form_input" >
      <option v-for="unit in listOfUnits" v-bind:value="unit.unit">{{unit.unit}}</option>
        </select>
</div>`,
    created: function () {
        unitsControllerApi.get().then(result => result.json().then(data => data.forEach(unit => {
                this.listOfUnits.push(unit)
            }
        )))
    },
    mounted() {
        this.units_input = this.selectedUnit; //присваиваем селектору первоначальное значение, которое присвоено пользователю
    },
    methods: {
        assignNewUnit() {
            skuApp.newObject.unit = this.units_input;
        }
    }
});

Vue.component('app-contractor-for-update', {
    props: {
        selectedContractorName: ''
    },
    data() {
        return {
            listOfContractors: [],
            contractors_input: ''
        }
    },
    template: `
<div>
<select v-model="contractors_input" @change="assignNewContractor" class="form_input" >
      <option v-for="contractor in listOfContractors" v-bind:value="contractor.name">{{contractor.name}} [{{contractor.contractorCode}}]</option>
        </select>
</div>`,
    created: function () {
        contractorsControllerApi.get().then(result => result.json().then(data => data.forEach(contractor => {
                this.listOfContractors.push(contractor)
            }
        )))
        this.contractors_input = this.selectedContractorName; //присваиваем селектору первоначальное значение, которое присвоено пользователю
    },
    methods: {
        assignNewContractor() {
            skuApp.newObject.contractorName = this.contractors_input;
        }
    }
});


var skuApp = new Vue({
    el: '#sku-app',
    data() {
        return {
            listOfSku: [],
            listOfContractors: [],
            listOfUnits: [],
            isAddPopupVisible: false,
            isUpdatePopupVisible: false,
            isDeletePopupVisible: false,
            newObject: {},
            nameErrorEnable: false,
            contractorsErrorEnable: false,
            pkgErrorEnable: false,
            error_message: '',
            toggleForUpdate: true,
        }
    },
    methods: {
        showAddPopup() {
            this.resetNewObject();
            this.isAddPopupVisible = true;
        },
        showUpdatePopup(skuId) {
            this.initObjectWithFieldsForUpdateOrDelete(skuId);
            this.isUpdatePopupVisible = true;
        },
        showDeletePopup(skuId) {
            this.initObjectWithFieldsForUpdateOrDelete(skuId);
            this.isDeletePopupVisible = true;
        },
        initObjectWithFieldsForUpdateOrDelete(skuId) {
            var sku = this.findSkuById(skuId);
            this.newObject = _.cloneDeep(sku); //клонируем объект методом библиотеки lodash
        },
        closeAddPopupWindow() {
            this.resetErrorStyles();
            this.isAddPopupVisible = false;

        },
        closeUpdatePopupWindow() {
            this.resetErrorStyles();
            this.isUpdatePopupVisible = false;
        },
        closeDeletePopupWindow() {
            this.resetErrorStyles();
            this.isDeletePopupVisible = false;
        },
        validateAndAdd() {
            let validateResult = this.validateAdd();
            if (validateResult) {
                this.sendAddData();
            }
        },
        validateAndUpdate() {
            let validateResult = this.validateUpdate();
            if (validateResult) {
                this.sendDataForUpdate();
            }
        },
        validateAdd() {
            let result = true;
            this.resetErrorStyles();
            if (!this.checkLength(this.newObject.name, 2, 255)) {
                this.error_message = "Наименование товара - обязательное поле, от 2 до 255 символов";
                this.nameErrorEnable = true;
                result = false;
            } else if (!this.checkContractorField(this.newObject.contractor.name)) {
                this.error_message = "Нужно выбрать поставщика";
                this.contractorsErrorEnable = true;
                result = false;
            } else if (!this.checkPkg(this.newObject.quantityInSecondaryPackaging)) {
                this.error_message = "Значение количества во вторичной упаковке должно быть числом больше 0";
                this.pkgErrorEnable = true;
                result = false;
            }
            return result;
        },
        validateUpdate() {
            let result = true;
            this.resetErrorStyles();
            if (this.newObject.name.length > 0 && !this.checkLength(this.newObject.name, 2, 255)) {
                this.error_message = "Наименование товара - может быть от 2 до 255 символов, или оставьте это поле пустым";
                this.nameErrorEnable = true;
                result = false;
            } else if (!this.checkPkg(this.newObject.quantityInSecondaryPackaging)) {
                this.error_message = "Значение количества во вторичной упаковке должно быть числом больше 0";
                this.pkgErrorEnable = true;
                result = false;
            }
            return result;
        },
        sendDataForDelete() {
            skuController.delete({id: this.newObject.id})
                .then(result => result.json().then(data => {
                    let deleteResult = data.deleteResult;
                    if (deleteResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeDeletePopupWindow();
                    } else {
                        this.error_message = deleteResult;
                    }
                }));
        },
        checkLength(string, minLength, maxLength) {
            let result = true;
            if (string == null) { //проверка на undefined
                result = false;
            } else if (string.length < minLength || string.length > maxLength) {
                result = false;
            }
            return result;
        },
        checkContractorField(string) {
            return string != null;
        },
        checkPkg(string) {
            let regex = new RegExp("^(\\d{1,9})$");
            return regex.test(string) && parseInt(string) > 0;
        },
        resetErrorStyles() {
            this.error_message = '';
            this.nameErrorEnable = false;
            this.contractorsErrorEnable = false;
            this.pkgErrorEnable = false
        },
        resetNewObject() {
            this.newObject = {
                code:"",
                name:"",
                contractor:{},
                active: true,
                quantityInSecondaryPackaging: 1
            };
        },
        findSkuById(id) {
            var foundSku;
            for (var i in this.listOfSku) {
                if (this.listOfSku[i].id === id) {
                    foundSku = this.listOfSku[i];
                    break;
                }
            }
            return foundSku;
        },
        sendAddData() {
            skuController.save(this.newObject)
                .then(result => result.json().then(data => {
                    let addResult = data.addResult;
                    if (addResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeAddPopupWindow();
                    } else {
                        this.error_message = addResult;
                    }
                }));
        },
        sendDataForUpdate() {
            skuController.update(this.newObject)
                .then(result => result.json().then(data => {
                    let updateResult = data.updateResult;
                    if (updateResult === 'OK') {
                        this.toggleForUpdate = !this.toggleForUpdate;
                        this.closeUpdatePopupWindow();
                    } else {
                        this.error_message = updateResult;
                    }
                }));
        },
    },
    computed: {
        countOfRows() {
            return this.listOfSku.length
        },
        sumOfPkg() {
            var sum = 0;
            for(var i = 0; i < this.listOfSku.length; i++) {
                sum += this.listOfSku[i].quantityInSecondaryPackaging
            }
            return sum;
        },
    },
    components: {
        appSku,
        filter_comp: 'app-filter-sku',
        table_header_comp: 'app-table-header-sku'
    }
});