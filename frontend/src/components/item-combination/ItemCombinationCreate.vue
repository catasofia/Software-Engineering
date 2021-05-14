<template>
  <v-container fluid class="ma-0 pa-0">
    <v-row class="mx-0 mt-5 mb-0" align="center">
      <h2 class="pt-0 my-0 mr-3">Column A</h2>
      <v-btn
        small
        color="primary"
        @click="addToColumnOne"
        data-cy="addToColumnOne"
      >
        Add Item A
      </v-btn>
    </v-row>
    <v-row
      v-for="(item, index) in sQuestionDetails.columnOne"
      :key="'a' + index"
      data-cy="questionItemsInputA"
      align="center"
      class="mx-0 mt-2"
    >
      <v-col cols="8" class="pl-0 py-0">
        <v-textarea
          v-model="item.content"
          :label="`Item ${item.internId}`"
          :data-cy="`Item${item.internId}`"
          rows="1"
          auto-grow
          class="mt-0"
        ></v-textarea>
      </v-col>
      <v-col class="pr-0 py-0">
        <v-select
          :items="sQuestionDetails.columnTwo"
          data-cy="combinations"
          item-text="internId"
          v-model="item.correctCombinations"
          label="Combinations"
          class="mt-0"
          multiple
          return-object
        >
          <template slot="selection" slot-scope="item">
            <v-chip style="height: 24px">{{
              'Item ' + Number(+item.item.internId)
            }}</v-chip>
          </template>

          <template v-slot:item="{ item, attrs, on }">
            <v-list-item v-on="on" v-bind="attrs" #default="{ active }">
              <v-list-item-action>
                <v-checkbox :input-value="active"></v-checkbox>
              </v-list-item-action>
              <v-list-item-content>
                <v-list-item-title>
                  <v-row no-gutters align="center">
                    <span>{{ 'Item ' + Number(+item.internId) }}</span>
                  </v-row>
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </template>
        </v-select>
      </v-col>
      <v-col cols="1" v-if="sQuestionDetails.columnOne.length > 1">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              :data-cy="`Delete${index + 1}`"
              small
              class="ma-1 action-button"
              v-on="on"
              @click="removeFromColumnOne(index)"
              color="red"
              >close</v-icon
            >
          </template>
          <span>Remove Item</span>
        </v-tooltip>
      </v-col>
    </v-row>

    <v-row class="mx-0 mt-5 mb-0" align="center">
      <h2 class="pt-0 my-0 mr-3">Column B</h2>
      <v-btn
        small
        color="primary"
        @click="addToColumnTwo"
        data-cy="addToColumnTwo"
      >
        Add Item B
      </v-btn>
    </v-row>
    <v-row
      v-for="(item, index) in sQuestionDetails.columnTwo"
      :key="'b' + index"
      align="center"
      class="mx-0 mt-2"
      data-cy="questionItemsInputB"
    >
      <v-col cols="8" class="pl-0 py-0">
        <v-textarea
          v-model="item.content"
          :label="`Item ${item.internId}`"
          :data-cy="`Item ${item.internId}`"
          rows="1"
          auto-grow
          class="mt-0"
        ></v-textarea>
      </v-col>
      <v-col cols="1" v-if="sQuestionDetails.columnTwo.length > 1">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              :data-cy="`Delete${index + 1}`"
              small
              class="ma-1 action-button"
              v-on="on"
              @click="removeFromColumnTwo(index)"
              color="red"
              >close</v-icon
            >
          </template>
          <span>Remove Item</span>
        </v-tooltip>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Model, PropSync, Vue, Watch } from 'vue-property-decorator';
import ItemCombinationQuestionDetails from '@/models/management/questions/ItemCombinationQuestionDetails';
import ItemCombinationSlot from '@/models/management/questions/ItemCombinationSlot';
@Component
export default class ItemCombinationCreate extends Vue {
  @PropSync('questionDetails', { type: ItemCombinationQuestionDetails })
  sQuestionDetails!: ItemCombinationQuestionDetails;

  combinations = [];

  internId = 0;

  created() {
    for (let item of this.sQuestionDetails.columnOne) {
      if (this.internId < item.internId) {
        this.internId = item.internId;
      }
    }
    for (let item of this.sQuestionDetails.columnTwo) {
      if (this.internId < item.internId) {
        this.internId = item.internId;
      }
    }

    this.internId++;

    if (this.sQuestionDetails.columnOne.length == 0) {
      this.sQuestionDetails.columnOne.push(
        new ItemCombinationSlot({ internId: this.internId++, column: 'a' })
      );
    }
    if (this.sQuestionDetails.columnTwo.length == 0) {
      this.sQuestionDetails.columnTwo.push(
        new ItemCombinationSlot({ internId: this.internId++, column: 'b' })
      );
    }
  }

  addToColumnOne() {
    this.sQuestionDetails.columnOne.push(
      new ItemCombinationSlot({ internId: this.internId++, column: 'a' })
    );
  }

  addToColumnTwo() {
    this.sQuestionDetails.columnTwo.push(
      new ItemCombinationSlot({ internId: this.internId++, column: 'b' })
    );
  }

  removeFromColumnOne(index: number) {
    this.sQuestionDetails.columnOne.splice(index, 1);
  }

  removeFromColumnTwo(index: number) {
    this.sQuestionDetails.columnTwo.splice(index, 1);
  }
}
</script>
