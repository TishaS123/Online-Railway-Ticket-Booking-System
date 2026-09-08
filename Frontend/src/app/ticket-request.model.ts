import { Passenger } from "./passenger.model";

export class TicketRequest {
  public trainId: number = 0;
  public source: string = '';
  public destination: string = '';
  public classType: string = '';
  public date: string = '';
  public passengers: Passenger[] = [];
}
