package SystemITR.JosueGuinea1A.Departamentos.Controller;

import SystemITR.JosueGuinea1A.Departamentos.DTO.DepartamentosDTO;
import SystemITR.JosueGuinea1A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/departamento")
@CrossOrigin
public class DepartamentosController {


    private final DepartamentosService service;

    public DepartamentosController(DepartamentosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentosDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentosDTO json){
        try{
            //Creamos un objeto DTO porque el service.insertarDatos retornará un objeto de tipo DepartamentosDTO
            DepartamentosDTO objDTO = service.insertarDatos(json);
            if (objDTO == null){
                log.warn("Intento de insercion fallido: "+json);
                ApiResponse respuesta = new ApiResponse(false,"No se pudo completar el proceso de inserción", json);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            log.info("Nuevos datos de departamento creados: "+ objDTO);
            ApiResponse respuesta = new ApiResponse(true, "Dato ingresado exitosamente", objDTO);
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            log.error("Error critico al crear, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuesta = new ApiResponse<>(false, "Error critico"+ e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentosDTO>>> obtenerDepartamentos(){
        try{
            List<DepartamentosDTO> listaDTO = service.listarTodos();
            if (listaDTO != null ){
                ApiResponse<List<DepartamentosDTO>> respuestaExitosa = new ApiResponse<>(true,"Proceso completado",listaDTO);
                return ResponseEntity.ok(respuestaExitosa);
            }
            ApiResponse<List<DepartamentosDTO>> respuestaNoData = new ApiResponse<>(true,"no hay datos q mostrar", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoData);
        }catch (Exception e){
            log.error("Error critico al obtener, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<List<DepartamentosDTO>> respuestaError = new ApiResponse<>(false,"No se pudo obtener datos ",null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @GetMapping("/{id}")
    //Aqui solo usamos DepartamentoDTO y no un <List> pq solo devuelve 1 valor, el PathVariable es lo q entra al metodo, el va a tomar el valor de la url y lo guarda en su variable
    public ResponseEntity<ApiResponse<DepartamentosDTO>> obtenerDepartamentosPorId(@PathVariable Long id){
        try{
            DepartamentosDTO DTO = service.buscarDepartamento(id);
            if (DTO != null){
                log.info("Se obtuvieron los datos del departamento: "+DTO);
                //Armar la respuesta usando ApiResponse
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true,"Dato encontrado con id"+id , DTO);
                return ResponseEntity.ok(respuestaExitosa);
            }
            log.info("Datos no encontrados con id: "+id);
            ApiResponse<DepartamentosDTO>  noEncontrado = new  ApiResponse<>(false, "datos no encontrados");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noEncontrado);
        }catch (Exception e){
            log.error("Error critico al obtener por id, consulte con el administrador");
            e.printStackTrace();
                //Usamos el nuevo constructor que se creo el cual no tiene data por que en el error no se recibe ningun dato
                ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"No se pudo obtener informacion con el ID: "+id);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @DeleteMapping ("/{id}")
    //En este metodo no se devuelve nada, pq es un delete
    public ResponseEntity<ApiResponse<Void>> eliminarDepartamento(@PathVariable Long id){
        try {
            boolean respuesta = service.eliminarInfo(id);
            if (respuesta) {
                log.info("Departamento con id: "+id +" eliminado");
            ApiResponse<Void> respuestaExitosa = new ApiResponse<>(true,"Dato con ID "+id+" eliminado exitosamente",null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaExitosa);
            }
            //Las siguientes lineas se ejecutaran solo si la eliminacion no se pudo eliminar
            log.info("Departamento con ID: "+id+" No fue encontrado");
            ApiResponse<Void> respuestaNoRealizado = new ApiResponse<>(false, "El proceso de eliminacion no se pudo completar debido a que no se encontro un ID", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);
        } catch (Exception e) {
            //log es un mensaje que queda guardado o registrado en el historial de el servidor
            log.error("Error critico al eliminar, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<Void> respuestaError = new ApiResponse<>(false,"Error inesperado, consulte con el administrador para solucionar el problema",null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }

    @PutMapping("/{id}")
    //el valid activa las validaciones escritas en el DTO
    public ResponseEntity<ApiResponse<DepartamentosDTO>> actualizarDepartamentos(@PathVariable Long id,@Valid @RequestBody DepartamentosDTO dto){
        try{
            DepartamentosDTO objdto = service.actualizarInfo(id,dto);
            if (objdto!=null){
                //Esto se ejecuta cuando el proceso si salio exitos
                log.info("Departamento con el id: "+id+" ha sido actuializado");
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true,"Proceso completado",dto);
                return  ResponseEntity.ok(respuestaExitosa);
            }
           log.warn("No se pudo completar la actualizacion del id: "+id);
            ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(false,"No se pudo completar el proceso de actualizacion");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoRealizado);

        }catch (Exception e){
            //log es un mensaje que queda registrado en el historial del servidor
            log.error("Error critico, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false, "Error inesperado,consulte con el administrador para solucionar el problema", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);

        }

    }

    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentosDTO>> buscarPorAbreviatura(@PathVariable String abreviatura){
        try {
            DepartamentosDTO data = service.buscarPorAbreviatura(abreviatura);
            if (data != null){
                //Esto se ejecuta cuando el proceso si salio exitos
                log.info("Departamento con la abr: "+abreviatura+" ha sido actuializado");
                ApiResponse<DepartamentosDTO> respuestaExitosa = new ApiResponse<>(true,"Departamento con la abr: "+abreviatura+" ha sido actuializado",data);
                return  ResponseEntity.ok(respuestaExitosa);
            }
            log.warn("No se pudo completar la actualizacion del id: "+abreviatura);
            ApiResponse<DepartamentosDTO> respuestaNoRealizado = new ApiResponse<>(false,"No se pudo completar el proceso de actualizacion");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoRealizado);

        } catch (Exception e){
            log.error("Error critico al obtener por id, consulte con el administrador");
            e.printStackTrace();
            //Usamos el nuevo constructor que se creo el cual no tiene data por que en el error no se recibe ningun dato
            ApiResponse<DepartamentosDTO> respuestaError = new ApiResponse<>(false,"No se pudo obtener informacion con la abreviatura: "+abreviatura);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuestaError);
        }
    }
}
