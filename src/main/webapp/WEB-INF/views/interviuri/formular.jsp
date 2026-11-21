<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="esteEditare" value="${not empty interviu.id}"/>
<c:set var="titluPagina" value="${esteEditare ? 'Editează interviul' : 'Interviu nou'}" scope="request"/>
<c:set var="meniuActiv" value="interviuri" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="row justify-content-center">
    <div class="col-lg-8">

        <h1 class="h3 mb-1">${titluPagina}</h1>
        <p class="text-muted mb-3">
            <c:out value="${aplicare.pozitie}"/> @ <strong><c:out value="${companie.nume}"/></strong>
            · aplicat pe ${aplicare.dataAplicarii}
        </p>

        <c:if test="${not empty eroare}">
            <div class="alert alert-danger"><i class="bi bi-exclamation-triangle"></i> <c:out value="${eroare}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/interviuri/salveaza" class="card shadow-sm">
            <div class="card-body">
                <input type="hidden" name="id" value="${interviu.id}">
                <input type="hidden" name="aplicareId" value="${aplicare.id}">

                <div class="row">
                    <div class="col-md-5 mb-3">
                        <label for="dataOra" class="form-label">Data și ora <span class="text-danger">*</span></label>
                        <input type="datetime-local" id="dataOra" name="dataOra" class="form-control" required
                               value="${interviu.dataOra}">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="tip" class="form-label">Tip <span class="text-danger">*</span></label>
                        <select id="tip" name="tip" class="form-select" required>
                            <c:forEach var="t" items="${tipuri}">
                                <option value="${t}" ${t == interviu.tip ? 'selected' : ''}>${t.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="rezultat" class="form-label">Rezultat <span class="text-danger">*</span></label>
                        <select id="rezultat" name="rezultat" class="form-select" required>
                            <c:forEach var="r" items="${rezultate}">
                                <option value="${r}" ${r == interviu.rezultat ? 'selected' : ''}>${r.eticheta}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="intervievator" class="form-label">Intervievator</label>
                    <input type="text" id="intervievator" name="intervievator" class="form-control" maxlength="100"
                           value="${fn:escapeXml(interviu.intervievator)}">
                </div>
                <div class="mb-3">
                    <label for="intrebari" class="form-label">Întrebări primite</label>
                    <textarea id="intrebari" name="intrebari" class="form-control" rows="3" maxlength="2000"><c:out value="${interviu.intrebari}"/></textarea>
                </div>
                <div class="mb-3">
                    <label for="impresii" class="form-label">Impresii</label>
                    <textarea id="impresii" name="impresii" class="form-control" rows="3" maxlength="2000"><c:out value="${interviu.impresii}"/></textarea>
                </div>
            </div>
            <div class="card-footer bg-white d-flex justify-content-end gap-2">
                <a href="${pageContext.request.contextPath}/aplicari/detalii?id=${aplicare.id}" class="btn btn-outline-secondary">Anulează</a>
                <button type="submit" class="btn btn-primary"><i class="bi bi-check-lg"></i> Salvează</button>
            </div>
        </form>

    </div>
</div>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>