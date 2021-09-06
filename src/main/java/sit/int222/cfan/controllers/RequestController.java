package sit.int222.cfan.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.Foodtypereq;
import sit.int222.cfan.entities.Ingredientsreq;
import sit.int222.cfan.entities.Request;
import sit.int222.cfan.entities.User;
import sit.int222.cfan.exceptions.BaseException;
import sit.int222.cfan.exceptions.ExceptionResponse;
import sit.int222.cfan.repositories.RequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestController {
    @Autowired
    RequestRepository requestRepository;

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request findById(Long id){
        Request request = requestRepository.findById(id).orElse(null);
        if(request == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.REQUEST_DOES_NOT_EXIST, "Request : id {" + id + "} does not exist !!");
        }
        return request;
    }

    public List<Request> findRequestsUser(User user) {
        return requestRepository.findAllByUser(user);
    }

    public Request findByIdUser(User user,Long id){
        Request request = requestRepository.findByUserAndRequestid(user,id);
        if(request == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.REQUEST_DOES_NOT_EXIST, "Request : id {" + id + "} does not exist !!");
        }
        return request;
    }

    public Request createRequest(User user,Request newrequest){
        Request request = new Request();
        request.setStatus(Request.Status.WAIT);
        request.setUser(user);
        request = requestRepository.save(request);
        if(newrequest.getFoodtypereq()!=null){
            Foodtypereq foodtypereq = newrequest.getFoodtypereq();
            foodtypereq.setRequest(request);
            request.setFoodtypereq(foodtypereq);
        }else if (newrequest.getIngredientsreq() != null){
            Ingredientsreq ingredientsreq = newrequest.getIngredientsreq();
            ingredientsreq.setRequest(request);
            request.setIngredientsreq(newrequest.getIngredientsreq());
        }else {
            requestRepository.delete(request);
            throw new BaseException(ExceptionResponse.ERROR_CODE.REQUEST_SUBMITTED_NOT_FOUND, "Request : submitted request was not found !!");
        }
        return requestRepository.save(request);
    }

    public Map<String,Boolean> deleteRequest(Request request){
        requestRepository.delete(request);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public Map<String,Boolean> deleteRequestUser(User user,Long id){
        Request request = findByIdUser(user,id);
        return deleteRequest(request);
    }

    public Map<String,Boolean> deleteRequestId(Long id){
        Request request = findById(id);
        return deleteRequest(request);
    }

    public Request changestatus(Long id,Request statusrequest){
        Request request = findById(id);
        if(statusrequest.getStatus() == null){
            throw new BaseException(ExceptionResponse.ERROR_CODE.REQUEST_STATUS_IS_NULLL, "Request : request status is void !!");
        }
        request.setStatus(statusrequest.getStatus());
        return requestRepository.save(request);
    }

    public Request.Status[] requestStatus() {
        return Request.Status.values();
    }
}
