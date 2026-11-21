<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titluPagina" value="Companii" scope="request"/>
<c:set var="meniuActiv" value="companii" scope="request"/>
<%@ include file="/WEB-INF/views/fragmente/antet.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <div>
        <h1 class="h3 mb-0">Companii</h1>
        <p class="text-muted mb-0">${fn:length(companii)} companii salvate</p>
    </div>
    <a href="${pageContext.request.contextPath}/companii/nou" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Adaugă companie
    </a>
</div>

<c:choose>
    <c:when test="${empty companii}">
        <div class="alert alert-info">
            Nu ai salvat încă nicio companie. Apasă „Adaugă companie” pentru a începe.
        </div>
    </c:when>
    <c:otherwise>
        <div class="card shadow-sm">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Nume</th>
                        <th>Domeniu</th>
                        <th>Oraș</th>
                        <th>Dimensiune</th>
                        <th>Website</th>
                        <th class="text-end">Acțiuni</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="companie" items="${companii}">
                        <tr>
                            <td class="fw-semibold">
                                <a href="${pageContext.request.contextPath}/companii/detalii?id=${companie.id}"
                                   class="text-decoration-none">
                                    <c:out value="${companie.nume}"/>
                                </a>
                            </td>
                            <td><c:out value="${companie.domeniu}" default="—"/></td>
                            <td><c:out value="${companie.oras}" default="—"/></td>
                            <td>
                                <c:if test="${not empty companie.dimensiune}">
                                    <span class="badge text-bg-secondary">${companie.dimensiune}</span>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty companie.website}">
                                        <a href="${fn:escapeXml(companie.website)}" target="_blank" rel="noopener">
                                            <i class="bi bi-box-arrow-up-right"></i> site
                                        </a>
                                    </c:when>
                                    <c:otherwise>—</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/companii/detalii?id=${companie.id}"
                                   class="btn btn-sm btn-outline-secondary" title="Detalii">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/companii/editare?id=${companie.id}"
                                   class="btn btn-sm btn-outline-primary" title="Editează">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                 <form method="post" action="${pageContext.request.contextPath}/companii/sterge"
                                          class="d-inline"
                                          onsubmit="return confirm('Sigur vrei să ștergi compania? Vor fi șterse și toate contactele ei.');">
                                        <input type="hidden" name="id" value="${companie.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger" title="Șterge">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/fragmente/subsol.jspf" %>