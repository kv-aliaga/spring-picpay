package org.example.picpayapisimples.handler.exception.model;

// Lançada quando model não é encontrado
public class ModelNotFoundException extends ModelException {
//    para models
    public ModelNotFoundException(String model, Long id) {
        super("Não foi possível encontrar " + model + " com o id " + id);
    }

//    para listas
    public ModelNotFoundException(String model){
        super("Não foi possível encontrar " + model);
    }
}
