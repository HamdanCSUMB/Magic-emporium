package com.example.magic_emporium.magic_emporiumDriver;

import java.util.Objects;
/*
Not a Acvitity, used to facilitate payment option functionality
 */

public class PaymentOption {
    private String label;
    private String type;
    private String cardNumber;
    private String note;

    public PaymentOption(String label, String type, String cardNumber, String note) {
        this.label = label;
        this.type = type;
        this.cardNumber = cardNumber;
        this.note = note;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentOption that = (PaymentOption) o;         //only comparing the actual values as user could label it something different and then it would count as different
        return type.equals(that.type) && cardNumber.equals(that.cardNumber) && note.equals(that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, cardNumber, note);    //only comparing the actual values as user could label it something different and then it would count as different
    }

    @Override
    public String toString() {
        return ""+this.getLabel()+"$"+this.getType()+"$"+this.getCardNumber()+"$"+this.getNote();   //creates the $ delimited string for utilization later, will use the , in the actual loop later
    }
}
