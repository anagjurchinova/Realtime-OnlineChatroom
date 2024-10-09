package com.example.onlinechatroomdemoproject.web;

import com.example.onlinechatroomdemoproject.bootstrap.DataHolder;
import com.example.onlinechatroomdemoproject.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/paypal")
public class PaymentController {
    private final PaypalService paypalService;

    public PaymentController(PaypalService paypalService){
        this.paypalService = paypalService;
    }

    @GetMapping
    public String paymentPage(@RequestParam(required = false) String error,
                              Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
            // TODO: 02.8.2024 show it in the html
        }
        return "payment-page";
    }

    @PostMapping("/create-payment") // /paypal/create-payment
    public String createPayment(@RequestParam String amount,
                                @RequestParam(required = false) String currency,
                                @RequestParam(required = false) String tip)
    {
        if(currency == null) currency = "USD";

        Double exchangeRate = DataHolder.conversionMap.get(currency);
        Double totalAmount = Double.parseDouble(amount) * exchangeRate;

        try{

            System.out.println(amount);
            String successUrl = "http://localhost:9000/payment-success-page";
            String cancelUrl = "http://localhost:9000/payment-cancel-page";

            Double tipAmount = null;
            if(tip == null) tip = "0.00";

            try {
                tipAmount = Double.parseDouble(tip);

            }catch (NumberFormatException ex){
                return "redirect:/paypal?error=Please enter a valid tip format.";
            }
            Double total = totalAmount + tipAmount;
            System.out.println(total);

            Payment payment = paypalService.createPayment(
                    total,
                    currency,
                    "paypal",
                    "sale",
                    "Payment description",
                    successUrl,
                    cancelUrl
            );

            Optional<Links> links = payment
                    .getLinks()
                    .stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst();

            if(links.isPresent()) return "redirect:" + links.get().getHref();

        }catch (PayPalRESTException ex){
            System.out.println(ex.getMessage());
            return "payment-cancel-page";
            // TODO: 01.8.2024 redirect error
        }

        return "redirect:/paypal/payment-success";
    }

    @GetMapping("/payment-success") // /paypal/payment-success
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            if(payment.getState().equals("approved")){
                return "payment-success-page";
            }

        }catch (PayPalRESTException ex){
            System.out.println(ex.getMessage());
            // TODO: 01.8.2024 redirect /payment/success but with error as a query param
            return "payment-error";
            // TODO: 02.8.2024 On the payment error page when you click Go Back it redirects to our Home Page 
        }

        return "payment-success-page";
    }

}
