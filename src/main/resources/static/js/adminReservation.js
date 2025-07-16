$(document).ready(function () {
    // 전체 선택
    $('#selectAll').click(function () {
        $('input[name="reservationNo"]').prop('checked', this.checked);
    });

    // 단일 취소
    $('.cancel-btn').click(function () {
        const no = $(this).data('no');
        if (confirm('정말 취소하시겠습니까?')) {
            $.post('/admin/cancelReservation', { reservationNo: no }, function (res) {
                if (res === 'success') {
                    alert('취소되었습니다.');
                    location.reload();
                } else {
                    alert('취소 실패');
                }
            });
        }
    });

    // 선택 취소
    $('#bulkCancelForm').submit(function (e) {
        e.preventDefault();
        const selected = $('input[name="reservationNo"]:checked');
        if (selected.length === 0) {
            alert('선택된 항목이 없습니다.');
            return;
        }

        if (!confirm('선택한 예약을 모두 취소하시겠습니까?')) return;

        selected.each(function () {
            const no = $(this).val();
            $.post('/admin/cancelReservation', { reservationNo: no }, function (res) {
                if (res === 'success') {
                    console.log('취소 완료: ' + no);
                }
            });
        });

        setTimeout(() => location.reload(), 500); // 잠깐 대기 후 새로고침
    });
});
