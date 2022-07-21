/*
 * Copyright (c) 2018. Prashant Kumar Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.info;

import com.info.types.HadoopRecord;
import com.info.types.LineItem;
import com.info.types.Notification;
import com.info.types.PosInvoice;

import java.util.ArrayList;
import java.util.List;


class RecordBuilder {
    static List<HadoopRecord> getHadoopRecords(PosInvoice invoice) {
        List<HadoopRecord> records = new ArrayList<>();
        for (LineItem i : invoice.getInvoiceLineItems()) {
            HadoopRecord record = new HadoopRecord()
                    .withInvoiceNumber(invoice.getInvoiceNumber())
                    .withCreatedTime(invoice.getCreatedTime())
                    .withStoreID(invoice.getStoreID())
                    .withPosID(invoice.getPosID())
                    .withCustomerType(invoice.getCustomerType())
                    .withPaymentMethod(invoice.getPaymentMethod())
                    .withDeliveryType(invoice.getDeliveryType())
                    .withItemCode(i.getItemCode())
                    .withItemDescription(i.getItemDescription())
                    .withItemPrice(i.getItemPrice())
                    .withItemQty(i.getItemQty())
                    .withTotalValue(i.getTotalValue());
            if (invoice.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY)) {
                record.setCity(invoice.getDeliveryAddress().getCity());
                record.setState(invoice.getDeliveryAddress().getState());
                record.setPinCode(invoice.getDeliveryAddress().getPinCode());
            }
            records.add(record);
        }
        return records;
    }

    static PosInvoice getMaskedInvoice(PosInvoice invoice) {
        invoice.setCustomerCardNo(null);
        if (invoice.getDeliveryType().equalsIgnoreCase(AppConfigs.DELIVERY_TYPE_HOME_DELIVERY)) {
            invoice.getDeliveryAddress().setAddressLine(null);
            invoice.getDeliveryAddress().setContactNumber(null);
        }
        return invoice;
    }

    static Notification getNotification(PosInvoice invoice) {
        try{
            Thread.sleep(1000);
        }catch (Exception e){e.printStackTrace();}
        return new Notification()
                .withInvoiceNumber(invoice.getInvoiceNumber())
                .withCustomerCardNo(invoice.getCustomerCardNo())
                .withTotalAmount(invoice.getTotalAmount())
                .withEarnedLoyaltyPoints(invoice.getTotalAmount() * AppConfigs.LOYALTY_FACTOR);
    }
}
