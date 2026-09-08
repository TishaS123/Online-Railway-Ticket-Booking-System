import { Time } from "@angular/common";
import { CoachesDTO } from "./coaches-dto.model";

export interface TrainResponseDTO{
    trainId : number;
    trainName : string;
    source : string;
    destination : string;
    arrivalTime : Time;
    departureTime: Time;
    date : Date;
    coaches: CoachesDTO[];
}