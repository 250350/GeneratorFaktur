package pl.comp.generatorfaktur.mapper;//package pl.comp.generatorfaktur.mapper;

import pl.comp.generatorfaktur.dto.InvoiceItem;
import pl.comp.generatorfaktur.dto.InvoiceRequest;
import pl.comp.generatorfaktur.entities.InvoiceEntity;
import pl.comp.generatorfaktur.entities.InvoiceItemEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class InvoiceMapper {

    public static InvoiceEntity toEntity(InvoiceRequest request) {

        InvoiceEntity entity = new InvoiceEntity();

        // SELLER

        entity.setCompanyNameSeller(request.getCompanyNameSeller());
        entity.setAddressSeller(request.getAddressSeller());
        entity.setPostalCodeAndCitySeller(request.getPostalCodeAndCitySeller());
        entity.setNipSeller(request.getNipSeller());

        // BUYER

        entity.setCompanyNameBuyer(request.getCompanyNameBuyer());
        entity.setAddressBuyer(request.getAddressBuyer());
        entity.setPostalCodeAndCityBuyer(request.getPostalCodeAndCityBuyer());
        entity.setNipBuyer(request.getNipBuyer());

        // TAXES

        entity.setVatRate(parseDouble(request.getStawkaVAT()));

        entity.setIrpfRate(request.getIrpf());

        entity.setApplyIRPF(request.isApplyIRPF());

        // INVOICE

        entity.setInvoiceNumber(request.getInvoiceNumber());

        entity.setCompletionOfServiceDate(request.getCompletionOfServiceDate());

        entity.setPaymentDate(request.getPaymentDate());

        // PAYMENT

        entity.setBankAccountNumber(request.getBankAccountNumber());

        entity.setSwift(request.getSwift());

        // ITEMS

        List<InvoiceItemEntity> items = request.getItems()
                .stream()
                .map(itemRequest -> {

                    InvoiceItemEntity item = new InvoiceItemEntity();

                    item.setDescription(itemRequest.getDescription());

                    item.setAmount(itemRequest.getAmount());

                    item.setNetPrice(itemRequest.getNetPrice());

                    item.setInvoice(entity);

                    return item;

                }).toList();

        entity.setItems(items);

        // TOTAL

        BigDecimal total = calculateTotal(
                request.getItems(),
                request.getStawkaVAT(),
                request.getIrpf()
        );

        entity.setAmount(total);

        entity.setStatus("UNPAID");

        return entity;
    }

    private static BigDecimal calculateTotal(
            List<InvoiceItem> items,
            String vat,
            double irpf
    ) {

        BigDecimal totalNet = BigDecimal.ZERO;

        for (InvoiceItem item : items) {

            BigDecimal price = BigDecimal.valueOf(item.getNetPrice());

            BigDecimal amount = BigDecimal.valueOf(item.getAmount());

            totalNet = totalNet.add(price.multiply(amount));
        }

        BigDecimal vatRate = new BigDecimal(vat).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal vatValue = totalNet.multiply(vatRate);

        BigDecimal irpfRate = BigDecimal.valueOf(irpf).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal irpfValue = totalNet.multiply(irpfRate);

        return totalNet.add(vatValue).subtract(irpfValue);
    }

    private static double parseDouble(String value) {

        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }
}

//
//import pl.comp.generatorfaktur.dto.InvoiceItem;
//import pl.comp.generatorfaktur.dto.InvoiceRequest;
//import pl.comp.generatorfaktur.entities.InvoiceEntity;
//import pl.comp.generatorfaktur.entities.InvoiceItemEntity;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//public class InvoiceMapper {
//
//    public static InvoiceEntity toEntity(InvoiceRequest request) {
//
//        InvoiceEntity entity = new InvoiceEntity();
//
//        entity.setCompanyNameBuyer(request.getCompanyNameBuyer());
//        entity.setAddressBuyer(request.getAddressBuyer());
//        entity.setNipBuyer(request.getNipBuyer());
//
//        entity.setStawkaVAT(parseDouble(request.getStawkaVAT()));
//        entity.setIrpf(request.getIrpf());
//
//        List<InvoiceItemEntity> items = request.getItems()
//                .stream()
//                .map(itemRequest -> {
//                    InvoiceItemEntity item = new InvoiceItemEntity();
//                    item.setDescription(itemRequest.getDescription());
//                    item.setAmount(itemRequest.getAmount());
//                    item.setNetPrice(itemRequest.getNetPrice());
//
//                    item.setInvoice(entity);
//
//                    return item;
//                }).toList();
//
//        entity.setItems(items);
//
//        BigDecimal total = calculateTotal(request.getItems(), request.getStawkaVAT(), request.getIrpf());
//        entity.setAmount(total);
//        System.out.println("Total: " + total);
//        entity.setInvoiceNumber(request.getInvoiceNumber());
//
//        entity.setStatus("UNPAID");
//
//        return entity;
//    }
//
//
//        private static BigDecimal calculateTotal(List<InvoiceItem> items, String vat, double irpf) {
//
//
//            BigDecimal totalNet = BigDecimal.ZERO;
//
//            for (InvoiceItem item : items) {
//
//                BigDecimal price = BigDecimal.valueOf(item.getNetPrice());
//                BigDecimal amount = BigDecimal.valueOf(item.getAmount());
//
//                BigDecimal itemTotal = price.multiply(amount);
//
//                totalNet = totalNet.add(itemTotal);
//            }
//
//            BigDecimal vatRate = new BigDecimal(vat)
//                    .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
//
//            BigDecimal vatValue = totalNet.multiply(vatRate);
//
//            BigDecimal irpfRate = BigDecimal.valueOf(irpf)
//                    .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
//
//            BigDecimal irpfValue = totalNet.multiply(irpfRate);
//
//            return totalNet
//                    .add(vatValue)
//                    .subtract(irpfValue);
//        }
//
//
//        private static double parseDouble(String value) {
//        try{
//            return  Double.parseDouble(value);
//        } catch (Exception e) {
//            return 0.0;
//        }
//    }
//}