package org.example.picpayapisimples.service.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.picpayapisimples.handler.exception.model.ActiveModelDeleteException;
import org.example.picpayapisimples.handler.exception.patch.ImmutableFieldException;
import org.example.picpayapisimples.handler.exception.model.InactiveResourceException;
import org.example.picpayapisimples.handler.exception.model.MinimumInactivityPeriodException;
import org.example.picpayapisimples.handler.exception.patch.InexistentFieldPatchException;
import org.example.picpayapisimples.handler.exception.model.ModelNotFoundException;
import org.example.picpayapisimples.mapper.BaseMapper;
import org.example.picpayapisimples.model.BaseModel;
import org.example.picpayapisimples.repository.BaseRepository;
import org.example.picpayapisimples.utils.PatchConfig;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public abstract class BaseService<REQ, RESP, MOD extends BaseModel>
implements BaseServiceCRD<REQ, RESP> {
    private final String className;
    protected BaseRepository<MOD> baseRepo;
    protected BaseMapper<REQ, RESP, MOD> mapper;

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseService(String className, BaseRepository<MOD> baseRepo, BaseMapper<REQ, RESP, MOD> mapper) {
        this.className = className;
        this.baseRepo = baseRepo;
        this.mapper = mapper;
    }

    protected void antesInserir(REQ dto, MOD mod){

    }

    public MOD getModel(Long id) {
        return baseRepo.findById(id).orElseThrow(() -> new ModelNotFoundException(className, id));
    }

    @Override
    public RESP buscarPorId(Long id) {
        MOD mod = getModel(id);

        return mapper.toResponse(mod);
    }

    @Override
    public List<RESP> buscarTodos() {
        List<MOD> modList = baseRepo.findAll();
        if (modList.isEmpty()) throw new ModelNotFoundException(className);

        return mapper.toResponseList(modList);
    }

    @Override
    public RESP inserir(REQ dto) {
        MOD mod = mapper.toModel(dto);

        antesInserir(dto, mod);
        baseRepo.save(mod);

        return mapper.toResponse(mod);
    }

    @Override
    public void excluir(Long id) {
        MOD mod = getModel(id);

        checkAtivo(mod.getAtivo());
        checkDataAtualizacao(mod.getDataAtualizacao());

        baseRepo.delete(mod);
    }

    //    Verifica se model já está há seis meses inativo para poder excluir
    private void checkDataAtualizacao(LocalDateTime ultimaAtualizacao){
        Integer mesesSemAtualizar = Math.toIntExact(ChronoUnit.MONTHS.between(ultimaAtualizacao, LocalDateTime.now()));
        if (mesesSemAtualizar < 5) throw new MinimumInactivityPeriodException(mesesSemAtualizar);
    }

//    Verifica se está ativo antes de excluir
    private void checkAtivo(Boolean ativo){
        if (ativo) throw new ActiveModelDeleteException(className);
    }

//    Verifica se está inativo antes de fazer alguma operação (ex.: Patch)
    protected void checkInativo(Boolean ativo){
        if (!ativo) throw new InactiveResourceException(className);
    }

//    Verifica se campo do patch pode ser alterado
    protected void checkCamposPatch(PatchConfig patchConfig, Map<String, Object> camposPatch){
        camposPatch.keySet().forEach(campo -> {
            if (!patchConfig.allCampos().contains(campo)) throw new InexistentFieldPatchException(campo, className);
            if (!patchConfig.patchableCampos().contains(campo)) throw new ImmutableFieldException(campo);
        });
    }
}
