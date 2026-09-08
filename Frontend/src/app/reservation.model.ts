import { Time } from "@angular/common";
import { TicketStatus } from "./ticket-status.model";

export class Reservation{
    public ticketNo: number = 0;
    public pnrNo : string = '';
    public trainName : string = '';
    public date : Date = new Date();
    public source : string = '';
    public destination : string = '';
    public arrivalTime: string ='';
    public departureTime : string = '';
    public status : TicketStatus = TicketStatus.CONFIRMED;
    public totalAmount : number = 0;
}